package com.practicum.playlistmaker.medialibrary.playlists.ui

import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist

sealed interface PlaylistsState {

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    object Empty : PlaylistsState

}