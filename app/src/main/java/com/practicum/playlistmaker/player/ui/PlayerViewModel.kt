package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.player.ui.models.AddToPlaylistState
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    application: Application,
    private var currentTrack: Track,
    private val favoritesInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    private var mediaPlayer = MediaPlayer()

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val progressLiveData = MutableLiveData<TrackProgress>()
    fun observeProgress(): LiveData<TrackProgress> = progressLiveData

    private val trackLiveData = MutableLiveData<Track>()
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    private val addToPlaylistLiveData = SingleLiveEvent<AddToPlaylistState>()
    fun observeAddState(): LiveData<AddToPlaylistState> = addToPlaylistLiveData

    private var updateProgressJob: Job? = null

    init {
        setTrack()
        progressLiveData.postValue(TrackProgress(0L))
    }

    private fun setTrack() {
        trackLiveData.postValue(currentTrack)
        mediaPlayer.setDataSource(currentTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateLiveData.postValue(PlayerState.Paused)
        }
        mediaPlayer.setOnCompletionListener {
            stateLiveData.postValue(PlayerState.Paused)
            updateProgressJob?.cancel()
            progressLiveData.postValue(TrackProgress(0))
        }
    }

    fun play() {
        mediaPlayer.start()
        stateLiveData.postValue(PlayerState.Playing)
        updateProgressJob = infiniteUpdatingProgress()
    }

    fun pause() {
        mediaPlayer.pause()
        stateLiveData.postValue(PlayerState.Paused)
        updateProgressJob?.cancel()
    }

    override fun onCleared() {
        updateProgressJob?.cancel()
        mediaPlayer.release()
    }

    private fun infiniteUpdatingProgress() = viewModelScope.launch {
        while (mediaPlayer.isPlaying) {
            progressLiveData.postValue(TrackProgress(mediaPlayer.currentPosition.toLong()))
            delay(TIMER_DURATION_MILLS)
        }
    }

    fun likeUnlike() {
        viewModelScope.launch {
            if (trackLiveData.value!!.isFavorite) {
                favoritesInteractor.deleteTrack(currentTrack)
            } else {
                favoritesInteractor.insertTrack(currentTrack)
            }
        }
        currentTrack.isFavorite = !currentTrack.isFavorite
        trackLiveData.postValue(currentTrack)
    }

    fun updatePlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlists ->
                    playlistsLiveData.postValue(playlists)
                }
        }
    }

    fun addToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            if (playlistInteractor.addTrackToPlaytist(playlist, currentTrack)) {
                addToPlaylistLiveData.postValue(AddToPlaylistState.AlreadyAdded(playlist))
            } else {
                addToPlaylistLiveData.postValue(AddToPlaylistState.Done(playlist))
                updatePlaylists()
            }
        }
    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L
    }

}