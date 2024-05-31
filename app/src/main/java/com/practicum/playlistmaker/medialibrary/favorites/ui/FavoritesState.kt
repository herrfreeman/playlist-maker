package com.practicum.playlistmaker.medialibrary.favorites.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {

    data class Content(
        val trackList: List<Track>
    ) : FavoritesState

    object Empty : FavoritesState

}