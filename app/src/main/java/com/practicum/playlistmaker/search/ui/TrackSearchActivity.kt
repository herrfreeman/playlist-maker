package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.ui.models.TrackSearchState

@Suppress("notifyDataSetChanged")
class TrackSearchActivity : AppCompatActivity() {

    private var searchString = SEARCH_STRING_DEFAULT
    private val adapter = TrackSearchAdapter { addToHistory(it); openTrack(it) }
    private val historyAdapter = TrackSearchAdapter { openTrack(it) }

    private lateinit var binding: ActivitySearchBinding
    var viewModel: TrackSearchViewModel? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchTextWatcher: TextWatcher
    private var openTrackAllowed = true

    @Suppress("notifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, TrackSearchViewModel.getViewModelFactory())[TrackSearchViewModel::class.java]
        viewModel?.observeState()?.observe(this) {render(it)}
        viewModel?.observeHistory()?.observe(this) {
            historyAdapter.trackList.clear()
            historyAdapter.trackList.addAll(it)
            historyAdapter.notifyDataSetChanged()
            setHistoryVisibility()
        }
        viewModel?.observeToastState()?.observe(this) {showToast(it)}

        binding.trackRecyclerView.adapter = adapter
        binding.historyRecyclerView.adapter = historyAdapter
        binding.repeatSearchButton.setOnClickListener { viewModel?.searchNow(searchString) }

        setCleanSearchButtonVisibility()

        binding.searchToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
                setCleanSearchButtonVisibility()
                setHistoryVisibility()
                hideErrorFrames()
                viewModel?.searchDebounce(searchString)
            }
            override fun afterTextChanged(p0: Editable?) {}
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel?.searchNow(searchString)
                binding.searchEditText.clearFocus()
                true
            } else false
        }

        binding.searchEditText.setOnFocusChangeListener { _, _ -> setHistoryVisibility() }


        binding.searchEditTextLayout.setEndIconOnClickListener {
            binding.searchEditText.setText(SEARCH_STRING_DEFAULT)
            viewModel?.clearTrackList()

            //Hide keyboard when clear button clicked
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.clearFocus()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel?.clearHistory()
        }

    }

    private fun setCleanSearchButtonVisibility() {
        binding.searchEditTextLayout.isEndIconVisible = searchString.isNotEmpty()
    }

    private fun setHistoryVisibility() {
        binding.historyLayout.isVisible = historyAdapter.trackList.isNotEmpty() && searchString.isEmpty() && binding.searchEditText.hasFocus()
    }

    private fun hideErrorFrames() {
        binding.connectionErrorFrame.isVisible = false
        binding.nothingFoundFrame.isVisible = false
    }

    private fun addToHistory(track: Track) {
        viewModel?.addToHistory(track)
    }

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
            is TrackSearchState.Loading -> showLoading()
            is TrackSearchState.Content -> showContent(state.trackList)
            is TrackSearchState.Error -> showError()
            is TrackSearchState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.nothingFoundFrame.visibility = View.VISIBLE
        binding.connectionErrorFrame.visibility = View.GONE
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.nothingFoundFrame.visibility = View.GONE
        binding.connectionErrorFrame.visibility = View.VISIBLE
    }

    private fun showContent(trackList: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.nothingFoundFrame.visibility = View.GONE
        binding.connectionErrorFrame.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val SEARCH_STRING_DEFAULT = ""
        const val OPEN_TRACK_DEBOUNCE = 300L
    }


}

