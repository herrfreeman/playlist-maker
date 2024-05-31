package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistsViewModel(
    application: Application,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    private var coverFileName: String = ""

    fun createPlaylist(name: String, description: String) {

        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    coverFileName = coverFileName,
                )
            )
        }
    }

    fun setCoverFileName(fileName: String) {
        coverFileName = fileName
    }

}