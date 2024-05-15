package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {

    data class Content(
        val trackList: List<Track>
    ) : FavoritesState

    object Empty : FavoritesState

}