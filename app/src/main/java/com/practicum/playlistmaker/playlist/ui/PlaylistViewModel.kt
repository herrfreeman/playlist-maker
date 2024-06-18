package com.practicum.playlistmaker.playlist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.TrackCountString
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    application: Application,
    private var currentPlaylist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val trackCounter: TrackCountString,
) : AndroidViewModel(application) {

    private var trackList = emptyList<Track>()

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
                trackList = it
                postPlaylistState()
            }
        }
    }

    private fun postPlaylistState() {
        stateLiveData.postValue(
            if (trackList.isEmpty()) {
                PlaylistState.EmptyTracks(currentPlaylist)
            } else {
                PlaylistState.Content(currentPlaylist, trackList)
            }
        )
    }

    fun updatePlaylist() {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(currentPlaylist).collect {
                currentPlaylist = it
                postPlaylistState()
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

    fun sharePlaylist(trackList: List<Track>) {
        var textToShare = "${currentPlaylist.name}\n"
        if (currentPlaylist.description.isNotEmpty()) {
            textToShare += "${currentPlaylist.description}\n"
        }
        val countString = trackCounter.convertCount(currentPlaylist.trackCount)
        textToShare += "${currentPlaylist.trackCount} $countString\n"

        for (i in trackList.indices) {
            val trackDuration = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(trackList[i].trackTimeMillis)
            textToShare += "${i + 1}. ${trackList[i].artistName} - ${trackList[i].trackName} ($trackDuration)\n"
        }
        sharingInteractor.shareText(textToShare)
    }

    fun getPlaylist(): Playlist {
        return currentPlaylist
    }

    fun deleteCurrentPlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(currentPlaylist)
        }
    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L
    }

}