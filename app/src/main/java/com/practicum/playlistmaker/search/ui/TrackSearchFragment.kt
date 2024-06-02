package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentTrackSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrackSearchFragment : BindingFragment<FragmentTrackSearchBinding>() {

    private var searchString = SEARCH_STRING_DEFAULT
    private val adapter = TrackSearchAdapter { clickDebounce { addToHistory(it); openTrack(it) } }
    private val historyAdapter =
        TrackSearchAdapter { clickDebounce { addToHistory(it); openTrack(it) } }
    private var isClickAllowed = true

    private val viewModel: TrackSearchViewModel by viewModel()
    private lateinit var searchTextWatcher: TextWatcher

    override fun createInflatedBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrackSearchBinding {
        return FragmentTrackSearchBinding.inflate(inflater, container, false)
    }

    @Suppress("notifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeHistory().observe(viewLifecycleOwner) {
            historyAdapter.trackList.clear()
            historyAdapter.trackList.addAll(it)
            historyAdapter.notifyDataSetChanged()
            setHistoryVisibility()
        }
        viewModel.observeToastState().observe(viewLifecycleOwner) { showToast(it) }

        binding.trackRecyclerView.adapter = adapter
        binding.historyRecyclerView.adapter = historyAdapter
        binding.repeatSearchButton.setOnClickListener {
            clickDebounce {
                viewModel.searchNow(
                    searchString
                )
            }
        }

        setCleanSearchButtonVisibility()

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
                setCleanSearchButtonVisibility()
                setHistoryVisibility()
                hideErrorFrames()
                viewModel.searchDebounce(searchString)
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchNow(searchString)
                binding.searchEditText.clearFocus()
                true
            } else false
        }

        binding.searchEditText.setOnFocusChangeListener { _, _ -> setHistoryVisibility() }

        binding.searchEditTextLayout.setEndIconOnClickListener {
            binding.searchEditText.setText(SEARCH_STRING_DEFAULT)
            viewModel.clearTrackList()

            //Hide keyboard when clear button clicked
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.clearFocus()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getSearchHistory()
        viewModel.updateSearchFavorites()
    }

    private fun setCleanSearchButtonVisibility() {
        binding.searchEditTextLayout.isEndIconVisible = searchString.isNotEmpty()
    }

    private fun setHistoryVisibility() {
        binding.historyLayout.isVisible =
            historyAdapter.trackList.isNotEmpty() && searchString.isEmpty() && binding.searchEditText.hasFocus()
    }

    private fun hideErrorFrames() {
        binding.connectionErrorFrame.isVisible = false
        binding.nothingFoundFrame.isVisible = false
    }

    private fun addToHistory(track: Track) {
        viewModel.addToHistory(track)
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            R.id.action_trackSearchFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
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

    @Suppress("notifyDataSetChanged")
    private fun showContent(trackList: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.nothingFoundFrame.visibility = View.GONE
        binding.connectionErrorFrame.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun clickDebounce(listener: () -> Unit) {
        if (isClickAllowed) {
            isClickAllowed = false
            listener()
            lifecycleScope.launch { delay(CLICK_DEBOUNCE_DELAY); isClickAllowed = true }
        }
    }

    companion object {
        const val SEARCH_STRING_DEFAULT = ""
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

}