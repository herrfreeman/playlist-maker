package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    application: Application,
    private val trackSearchInteractor: TrackSearchInteractor,
    private val trackHistoryInteractor: TrackSearchHistoryInteractor,
) : AndroidViewModel(application) {

    private var latestSearchText: String? = null
    private val trackList = mutableListOf<Track>()

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeState(): LiveData<TrackSearchState> = stateLiveData

    private val historyLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    private val toastState = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = toastState

    var searchJob: Job? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    init {
        getSearchHistory()
    }

    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    fun searchNow(queryText: String) {
        searchDebounce(queryText, debounceDelay = 0, force = true)
    }

    fun searchDebounce(
        changedText: String,
        debounceDelay: Long = SEARCH_DEBOUNCE_DELAY,
        force: Boolean = false
    ) {
        if (latestSearchText == changedText && !force) {
            return
        }
        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debounceDelay)
            Log.d("PLAYER_DEBUG", "Search $changedText")
            searchRequest(changedText)
        }
    }

    fun addToHistory(track: Track) {
        viewModelScope.launch {
            trackHistoryInteractor.addToHistory(track)
                .collect{ tracks ->
                    historyLiveData.postValue(tracks)
                }
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            trackHistoryInteractor.getSearchHistory()
                .collect{ tracks ->
                    historyLiveData.postValue(tracks)
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            trackHistoryInteractor.clearHistory()
                .collect{ tracks ->
                    historyLiveData.postValue(tracks)
                }
        }
    }

    fun clearTrackList() {
        trackList.clear()
        stateLiveData.postValue(TrackSearchState.Content(trackList))
    }

    suspend fun searchRequest(queryText: String) {
        if (queryText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)
            trackSearchInteractor.searchTracks(queryText)
                .collect {
                    it.first?.let { tracks -> trackList.clear(); trackList.addAll(tracks) }

                    when {
                        it.second != null -> {
                            renderState(TrackSearchState.Error)
                            showToast(it.second!!)
                        }

                        trackList.isEmpty() -> renderState(TrackSearchState.Empty)
                        else -> renderState(TrackSearchState.Content(trackList))
                    }
                }

        }
    }

    fun showToast(message: String) {
        toastState.postValue(message)
    }

}