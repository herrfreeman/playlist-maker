package com.practicum.playlistmaker.playlist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistViewModel(
    application: Application,
    private var currentPlaylist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    private val toastState = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = toastState

    init {
        playlistLiveData.postValue(currentPlaylist)
        getTracks()
    }

    fun getTracks() {
        viewModelScope.launch {
            playlistInteractor.getTracksInPlaylist(currentPlaylist).collect {
                stateLiveData.postValue(
                    if (it.isEmpty()) {
                        PlaylistState.Empty
                    } else {
                        PlaylistState.Content(it)
                    }
                )
            }
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(currentPlaylist).collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track, currentPlaylist)
            updatePlaylist()
            getTracks()
        }

    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L
    }

}