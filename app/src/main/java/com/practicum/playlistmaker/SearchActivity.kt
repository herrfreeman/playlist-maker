package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class SearchActivity : AppCompatActivity() {

    private var searchString = SEARCH_STRING_DEFAULT
    private val ItunesBaseUrl = "https://itunes.apple.com/"
    private val gson = Gson()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val trackList: MutableList<Track> = emptyList<Track>().toMutableList()
    private val adapter = TrackAdapter(trackList)

    private lateinit var searchTextEditLayout: TextInputLayout
    private lateinit var searchTextEdit: TextInputEditText
    private lateinit var nothingFoundFrame: View
    private lateinit var connectionErrorFrame: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchTextEditLayout = findViewById(R.id.search_edit_text_layout)
        searchTextEdit = findViewById(R.id.search_edit_text)
        nothingFoundFrame = findViewById(R.id.nothing_found_frame)
        connectionErrorFrame = findViewById(R.id.connection_error_frame)

        val updateButton = findViewById<Button>(R.id.repeat_search_button)
        updateButton.setOnClickListener {songSearch()}

        setCleanSearchButtonVisibility()

        val topToolbar = findViewById<MaterialToolbar>(R.id.search_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
                setCleanSearchButtonVisibility()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)

        searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                songSearch()
                searchTextEdit.clearFocus()
                true
            }
            false
        }

        //Hide keyboard when clear button clicked
        searchTextEditLayout.setEndIconOnClickListener {
            searchTextEdit.setText(SEARCH_STRING_DEFAULT)
            trackList.clear()
            adapter.notifyDataSetChanged()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchTextEdit.windowToken, 0)
            searchTextEdit.clearFocus()
        }

        val recycler = findViewById<RecyclerView>(R.id.track_recycler_view)
        recycler.adapter = adapter

    }

    fun setCleanSearchButtonVisibility() {
        searchTextEditLayout.isEndIconVisible = !searchString.isEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchString)
        outState.putBoolean(CONNECTION_ERROR_VISIBLE, connectionErrorFrame.isVisible)
        outState.putBoolean(NOTHING_FOUND_VISIBLE, nothingFoundFrame.isVisible)
        outState.putString(TRACK_LIST, gson.toJson(Tracks(trackList)))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH_STRING, SEARCH_STRING_DEFAULT)
            ?: SEARCH_STRING_DEFAULT
        searchTextEdit.setText(searchString)

        trackList.clear()
        trackList.addAll(gson.fromJson(savedInstanceState.getString(TRACK_LIST), Tracks::class.java).list)
        adapter.notifyDataSetChanged()
        connectionErrorFrame.isVisible = savedInstanceState.getBoolean(CONNECTION_ERROR_VISIBLE, false)
        nothingFoundFrame.isVisible = savedInstanceState.getBoolean(NOTHING_FOUND_VISIBLE, false)

    }

    private fun songSearch() {
        itunesService.songSearch(searchString)
            .enqueue(object : Callback<SongSearchResponse> {
                override fun onResponse(
                    call: Call<SongSearchResponse>,
                    response: Response<SongSearchResponse>
                ) {
                    val searchResponse = response.body()

                    if (response.isSuccessful) {
                        trackList.clear()
                        trackList.addAll(
                            searchResponse?.results ?: emptyList<Track>()
                        )
                        adapter.notifyDataSetChanged()
                        connectionErrorFrame.isVisible = false
                        nothingFoundFrame.isVisible = trackList.isEmpty()
                    }

                }

                override fun onFailure(call: Call<SongSearchResponse>, t: Throwable) {
                    connectionErrorFrame.isVisible = true
                    nothingFoundFrame.isVisible = false
                }
            })
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_STRING_DEFAULT = ""
        const val CONNECTION_ERROR_VISIBLE = "CONNECTION_ERROR_VISIBLE"
        const val NOTHING_FOUND_VISIBLE = "NOTHING_FOUND_VISIBLE"
        const val TRACK_LIST = "TRACK_LIST"
    }
}

class SongSearchResponse(val results: List<Track>)
class Tracks(val list: MutableList<Track>)
interface ItunesApi {

    @GET("/search?entity=song")
    fun songSearch(
        @Query("term") text: String
    ): Call<SongSearchResponse>

}