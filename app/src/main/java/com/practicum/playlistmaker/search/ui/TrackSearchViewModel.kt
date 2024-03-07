package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState

class TrackSearchViewModel(application: Application) : AndroidViewModel(application) {

    private var latestSearchText: String? = null
    private val trackSearchInteractor = Creator.provideTrackSearchInteractor(getApplication<Application>())
    private val handler = Handler(Looper.getMainLooper())
    private val trackList = mutableListOf<Track>()

    private val stateLiveData = MutableLiveData<TrackSearchState>()

//    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->
//        liveData.addSource(stateLiveData) { movieState ->
//            liveData.value = when (movieState) {
//                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
//                is MoviesState.Empty -> movieState
//                is MoviesState.Error -> movieState
//                is MoviesState.Loading -> movieState
//            }
//        }
//    }

    fun observeState(): LiveData<TrackSearchState> = stateLiveData
//    fun observeState(): LiveData<TrackSearchState> = mediatorStateLiveData

    //private val toastState = MutableLiveData<String>()
//    private val toastState = SingleLiveEvent<String>()
//    fun observeToastState(): LiveData<String> = toastState

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackSearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    fun searchNow(queryText: String) {
        searchDebounce(queryText, 0)
    }

    fun searchDebounce(changedText: String, debounceDelay: Long = SEARCH_DEBOUNCE_DELAY) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (latestSearchText == changedText) {
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
                                renderState(
                                    TrackSearchState.Error(
                                        getApplication<Application>().getString(
                                            R.string.something_went_wrong
                                        )
                                    )
                                )
                                //showToast(errorMessage)
                            }

                            trackList.isEmpty() -> {
                                renderState(
                                    TrackSearchState.Empty(
                                        getApplication<Application>().getString(
                                            R.string.nothing_found
                                        )
                                    )
                                )
                            }

                            else -> {
                                renderState(TrackSearchState.Content(trackList))
                            }
                        }

                    }
                })
        }
    }

//    fun showToast(message: String) {
//        toastState.postValue(message)
//    }

//    fun toggleFavorite(movie: Movie) {
//        if (movie.inFavorite) {
//            moviesInteractor.removeMovieFromFavorites(movie)
//        } else {
//            moviesInteractor.addMovieToFavorites(movie)
//        }
//        updateMovieContent(movie.id, movie.copy(inFavorite = !movie.inFavorite))
//    }

//    private fun updateMovieContent(movieId: String, newMovie: Movie) {
//        val currentState = stateLiveData.value
//
//        if (currentState is TrackSearchState.Content) {
//            val movieIndex = currentState.movies.indexOfFirst { it.id == movieId }
//            if (movieIndex != -1) {
//                stateLiveData.value = TrackSearchState.Content(
//                    currentState.movies.toMutableList().also {
//                        it[movieIndex] = newMovie
//                    }
//                )
//            }
//        }
//    }


}