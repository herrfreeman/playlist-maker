package com.practicum.playlistmaker.medialibrary.playlists.ui

sealed interface CreatePlaylistState {

    object FileLoading : CreatePlaylistState
    data class PlaylistCreated(val playlistName: String) : CreatePlaylistState

}