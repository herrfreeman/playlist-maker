package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackSearchInteractor {

    fun searchTracks(expression: String, consumer: TrackSearchConsumer)

    interface TrackSearchConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}