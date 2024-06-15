package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface PlaylistState {

    data class Content(
        val trackList: List<Track>
    ) : PlaylistState

    object Empty : PlaylistState

}