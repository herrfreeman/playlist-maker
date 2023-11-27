package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_STRING_DEFAULT = ""
    }

    private var searchString = SEARCH_STRING_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setCleanSearchButtonVisibility()

        val topToolbar = findViewById<MaterialToolbar>(R.id.search_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
                setCleanSearchButtonVisibility()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)

        val searchTextEditLayout = findViewById<TextInputLayout>(R.id.search_edit_text_layout)
        searchTextEditLayout.setEndIconOnClickListener {
            searchTextEdit.setText(SEARCH_STRING_DEFAULT)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchTextEdit.windowToken, 0)
            searchTextEdit.clearFocus()
        }

        val recycler = findViewById<RecyclerView>(R.id.track_recycler_view)
        recycler.adapter = TrackAdapter(getTrackMockList())

    }

    fun setCleanSearchButtonVisibility() {
        val searchTextEditLayout = findViewById<TextInputLayout>(R.id.search_edit_text_layout)
        searchTextEditLayout.isEndIconVisible = !searchString.isEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH_STRING, SEARCH_STRING_DEFAULT)
            ?: SEARCH_STRING_DEFAULT
        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        searchTextEdit.setText(searchString)
    }


    data class Track(
        val trackName: String,
        val artistName: String,
        val trackTime: String,
        val artworkUrl100: String
    )

    class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track_item, parent, false)
    ) {

        private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val trackArtist: TextView = itemView.findViewById(R.id.track_description)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder)
                .transform(RoundedCorners(2))
                .into(trackImage)

            trackName.text = model.trackName
            trackArtist.text = "${model.artistName} • ${model.trackTime}"
        }
    }

    class TrackAdapter(val trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackViewHolder(parent)
        override fun getItemCount(): Int = trackList.size
        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            holder.bind(trackList[position])
        }

    }
}

private fun getTrackMockList() = listOf(
    SearchActivity.Track(
        "Smells Like Teen Spirit",
        "Nirvana",
        "5:01",
        "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    ),
    SearchActivity.Track(
        "Billie Jean",
        "Michael Jackson",
        "4:35",
        "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    ),
    SearchActivity.Track(
        "Stayin' Alive",
        "Bee Gees",
        "4:10",
        "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    ),
    SearchActivity.Track(
        "Whole Lotta Love",
        "Led Zeppelin",
        "5:33",
        "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    ),
    SearchActivity.Track(
        "Sweet Child O'Mine",
        "Guns N' Roses",
        "5:03",
        "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    )
)