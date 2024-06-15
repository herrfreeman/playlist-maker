package com.practicum.playlistmaker.playlist.ui

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.player.ui.models.AddToPlaylistState
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    application: Application,
    private var currentPlaylist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private val toastState = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = toastState

    init {
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



//    fun likeUnlike() {
//        viewModelScope.launch {
//            if (trackLiveData.value!!.isFavorite) {
//                favoritesInteractor.deleteTrack(currentTrack)
//            } else {
//                favoritesInteractor.insertTrack(currentTrack)
//            }
//        }
//        currentTrack.isFavorite = !currentTrack.isFavorite
//        trackLiveData.postValue(currentTrack)
//    }

//    fun updatePlaylists() {
//        viewModelScope.launch {
//            playlistInteractor.getPlaylists()
//                .collect { playlists ->
//                    playlistsLiveData.postValue(playlists)
//                }
//        }
//    }

//    fun addToPlaylist(playlist: Playlist) {
//        viewModelScope.launch {
//            if (playlistInteractor.addTrackToPlaytist(playlist, currentTrack)) {
//                addToPlaylistLiveData.postValue(AddToPlaylistState.AlreadyAdded(playlist))
//            } else {
//                addToPlaylistLiveData.postValue(AddToPlaylistState.Done(playlist))
//                updatePlaylists()
//            }
//        }
//    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L
    }

}