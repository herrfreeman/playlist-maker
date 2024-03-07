package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.player.data.presentation.PlayerActivity
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class TrackSearchActivity : AppCompatActivity() {

    private var searchString = SEARCH_STRING_DEFAULT
//    private val trackList: MutableList<Track> = emptyList<Track>().toMutableList()
    private val adapter = TrackSearchAdapter(
        object : TrackSearchAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                openTrack(track)
            }
        }
    )

//    private val historyTrackList: MutableList<Track> = emptyList<Track>().toMutableList()
//    private val historyAdapter = TrackSearchAdapter(historyTrackList) { openTrack(it) }

    lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: TrackSearchViewModel
//    private lateinit var appPreferences: SharedPreferences
    private val handler = Handler(Looper.getMainLooper())
    lateinit var searchTextWatcher: TextWatcher
//    val runSearch = Runnable { songSearch() }
    private var openTrackAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this, TrackSearchViewModel.getViewModelFactory())[TrackSearchViewModel::class.java]
        viewModel.observeState().observe(this) {render(it)}
//        viewModel.observeToastState().observe(this) {showToast(it)}

//        appPreferences = (applicationContext as App).appPreferences
//        appPreferences.getString(HISTORY_LIST, null)?.let {
//            historyTrackList.addAll(gson.fromJson(it, Array<Track>::class.java))
//        }

        binding.repeatSearchButton.setOnClickListener { /*songSearch()*/ }

        setCleanSearchButtonVisibility()
        setHistoryVisibility()

        binding.searchToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                searchString = p0.toString()
//                setCleanSearchButtonVisibility()
//                setHistoryVisibility()
//                hideErrorFrames()
//                handler.removeCallbacks(runSearch)
//                handler.postDelayed(runSearch, SEARCH_DEBOUNCE_MILLS)
            }

            override fun afterTextChanged(p0: Editable?) {
                searchString = p0.toString()
                viewModel?.searchDebounce(searchString)
            }
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

//        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                songSearch()
//                binding.searchEditText.clearFocus()
//                true
//            }
//            false
//        }

        binding.searchEditText.setOnFocusChangeListener { _, _ -> setHistoryVisibility() }


//        binding.searchEditTextLayout.setEndIconOnClickListener {
//            binding.searchEditText.setText(SEARCH_STRING_DEFAULT)
//            trackList.clear()
//            adapter.notifyDataSetChanged()
//            hideErrorFrames()
//
//            //Hide keyboard when clear button clicked
//            val inputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
//            binding.searchEditText.clearFocus()
//        }

        binding.trackRecyclerView.adapter = adapter
        //binding.historyRecyclerView.adapter = historyAdapter

//        binding.clearHistoryButton.setOnClickListener {
//            historyTrackList.clear()
//            historyAdapter.notifyDataSetChanged()
//            setHistoryVisibility()
//        }

    }

    private fun setCleanSearchButtonVisibility() {
        binding.searchEditTextLayout.isEndIconVisible = searchString.isNotEmpty()
    }

    private fun setHistoryVisibility() {
        binding.historyLayout.isVisible = false
//            historyTrackList.isNotEmpty() && searchString.isEmpty() && binding.searchEditText.hasFocus()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(SEARCH_STRING, searchString)
//        outState.putBoolean(CONNECTION_ERROR_VISIBLE, binding.connectionErrorFrame.isVisible)
//        outState.putBoolean(NOTHING_FOUND_VISIBLE, binding.nothingFoundFrame.isVisible)
//        outState.putString(TRACK_LIST, gson.toJson(trackList))
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        searchString = savedInstanceState.getString(SEARCH_STRING, SEARCH_STRING_DEFAULT)
//            ?: SEARCH_STRING_DEFAULT
//        binding.searchEditText.setText(searchString)
//
//        trackList.clear()
//        trackList.addAll(
//            gson.fromJson(
//                savedInstanceState.getString(TRACK_LIST),
//                Array<Track>::class.java
//            )
//        )
//        adapter.notifyDataSetChanged()
//        binding.connectionErrorFrame.isVisible =
//            savedInstanceState.getBoolean(CONNECTION_ERROR_VISIBLE, false)
//        binding.nothingFoundFrame.isVisible =
//            savedInstanceState.getBoolean(NOTHING_FOUND_VISIBLE, false)
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        appPreferences.edit()
//            .putString(HISTORY_LIST, gson.toJson(historyTrackList))
//            .apply()
//    }

//    private fun hideErrorFrames() {
//        binding.connectionErrorFrame.isVisible = false
//        binding.nothingFoundFrame.isVisible = false
//    }

//    private fun songSearch() {
//        if (searchString.isNotEmpty()) {
//            binding.progressBar.isVisible = true
//            itunesService.songSearch(searchString)
//                .enqueue(object : Callback<SongSearchResponse> {
//                    override fun onResponse(
//                        call: Call<SongSearchResponse>,
//                        response: Response<SongSearchResponse>
//                    ) {
//                        val searchResponse = response.body()
//
//                        if (response.isSuccessful) {
//                            trackList.clear()
//                            trackList.addAll(
//                                searchResponse?.results ?: emptyList<Track>()
//                            )
//                            adapter.notifyDataSetChanged()
//                            binding.connectionErrorFrame.isVisible = false
//                            binding.nothingFoundFrame.isVisible = trackList.isEmpty()
//                        }
//                        binding.progressBar.isVisible = false
//                    }
//
//                    override fun onFailure(call: Call<SongSearchResponse>, t: Throwable) {
//                        binding.connectionErrorFrame.isVisible = true
//                        binding.nothingFoundFrame.isVisible = false
//                    }
//                })
//        }
//    }

//    private fun addToHistory(track: Track) {
//        val existingTrack = historyTrackList.find { it.trackId == track.trackId }
//        if (existingTrack == null) {
//            for (i in HISTORY_SIZE..historyTrackList.count()) {
//                historyTrackList.removeAt(i-1)
//                historyAdapter.notifyItemRemoved(i-1)
//            }
//            historyTrackList.add(0, track)
//            historyAdapter.notifyItemInserted(0)
//        } else if (existingTrack.trackId > 0) {
//            val existingIndex = historyTrackList.indexOf(existingTrack)
//            historyTrackList.remove(existingTrack)
//            historyTrackList.add(0, track)
//            historyAdapter.notifyItemMoved(existingIndex, 0)
//        }
//        setHistoryVisibility()
//    }

    private fun openTrack(track: Track) {
        if (openTrackAllowed) {
            openTrackAllowed = false
            handler.postDelayed({ openTrackAllowed = true }, OPEN_TRACK_DEBOUNCE)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(Track.EXTRAS_KEY, track)
            startActivity(intent)
        }
    }

    fun render(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.Loading -> {}//showLoading()
            is TrackSearchState.Content -> showContent(state.trackList)
            is TrackSearchState.Error -> {}//showError(state.errorMessage)
            is TrackSearchState.Empty -> {}//showEmpty(state.message)
        }
    }

    fun showContent(trackList: List<Track>) {
        //binding.movieList.visibility = View.VISIBLE
        //binding.moviePlaceholderMessage.visibility = View.GONE
        //bindings.progressBar.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_STRING_DEFAULT = ""
        const val CONNECTION_ERROR_VISIBLE = "CONNECTION_ERROR_VISIBLE"
        const val NOTHING_FOUND_VISIBLE = "NOTHING_FOUND_VISIBLE"
        const val TRACK_LIST = "TRACK_LIST"
        const val HISTORY_SIZE = 10
        const val HISTORY_LIST = "SEARCH_HISTORY"
        const val SEARCH_DEBOUNCE_MILLS = 1000L
        const val OPEN_TRACK_DEBOUNCE = 300L
    }


}

//class SongSearchResponse(val results: List<Track>)
//
//interface ItunesApi {
//
//    @GET("/search?entity=song")
//    fun songSearch(
//        @Query("term") text: String
//    ): Call<SongSearchResponse>
//
//}