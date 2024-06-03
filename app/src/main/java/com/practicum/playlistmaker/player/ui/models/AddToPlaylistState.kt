package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist

sealed interface AddToPlaylistState {

    data class AlreadyAdded(val playlist: Playlist): AddToPlaylistState
    data class Done(val playlist: Playlist): AddToPlaylistState

}