package com.practicum.playlistmaker.medialibrary.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun updatePlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        stateLiveData.postValue(PlaylistsState.Empty)
                    } else {
                        stateLiveData.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }
}