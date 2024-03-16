package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import com.practicum.playlistmaker.utils.SingleLiveEvent

class TrackSearchViewModel(
    application: Application,
    private val trackSearchInteractor: TrackSearchInteractor,
    private val trackHistoryInteractor: TrackSearchHistoryInteractor,
) : AndroidViewModel(application) {

    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private val trackList = mutableListOf<Track>()

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeState(): LiveData<TrackSearchState> = stateLiveData

    private val historyLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    private val toastState = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = toastState

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    init {
        getSearchHistory()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (latestSearchText == changedText && !force) {
            return
        }

        this.latestSearchText = changedText

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + debounceDelay
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun addToHistory(track: Track) {
        trackHistoryInteractor.addToHistory(track,
            object : TrackSearchHistoryInteractor.HistoryChangeConsumer {
                override fun consume(tracks: List<Track>) {
                    historyLiveData.postValue(tracks)
                }
            })
    }

    fun getSearchHistory() {
        trackHistoryInteractor.getSearchHistory(
            object : TrackSearchHistoryInteractor.HistoryChangeConsumer {
                override fun consume(tracks: List<Track>) {
                    historyLiveData.postValue(tracks)
                }
            })
    }

    fun clearHistory() {
        trackHistoryInteractor.clearHistory(
            object : TrackSearchHistoryInteractor.HistoryChangeConsumer {
                override fun consume(tracks: List<Track>) {
                    historyLiveData.postValue(tracks)
                }
            })
    }

    fun clearTrackList() {
        trackList.clear()
        stateLiveData.postValue(TrackSearchState.Content(trackList))
    }

    fun searchRequest(queryText: String) {
        if (queryText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)

            trackSearchInteractor.searchTracks(
                queryText,
                object : TrackSearchInteractor.TrackSearchConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {

                        if (foundTracks != null) {
                            trackList.clear()
                            trackList.addAll(foundTracks)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(TrackSearchState.Error)
                                showToast(errorMessage)
                            }

                            trackList.isEmpty() -> renderState(TrackSearchState.Empty)
                            else -> renderState(TrackSearchState.Content(trackList))
                        }

                    }
                })
        }
    }

    fun showToast(message: String) {
        toastState.postValue(message)
    }

}