package com.practicum.playlistmaker.medialibrary.playlists.ui

sealed interface EditPlaylistState {

    object FileLoading : EditPlaylistState
    data class PlaylistCreated(val playlistName: String) : EditPlaylistState
    object PlaylistUpdated : EditPlaylistState

}