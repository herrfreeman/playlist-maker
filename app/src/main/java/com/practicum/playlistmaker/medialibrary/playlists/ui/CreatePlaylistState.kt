package com.practicum.playlistmaker.medialibrary.playlists.ui

sealed interface CreatePlaylistState {

    object Nothing : CreatePlaylistState
    object FileLoading : CreatePlaylistState
    object FileLoaded : CreatePlaylistState
    object PlaylistCreated : CreatePlaylistState

}