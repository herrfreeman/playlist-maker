package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {

    object Loading : TrackSearchState

    data class Content(
        val trackList: List<Track>
    ) : TrackSearchState

    object Error : TrackSearchState

    object Empty : TrackSearchState

}