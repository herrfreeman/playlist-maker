package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {

    object Loading : TrackSearchState

    data class Content(
        val trackList: List<Track>
    ) : TrackSearchState

    data class Error(
        val errorMessage: String
    ) : TrackSearchState

    data class Empty(
        val message: String
    ) : TrackSearchState

}