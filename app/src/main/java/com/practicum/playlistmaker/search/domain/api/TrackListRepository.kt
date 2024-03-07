package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource

interface TrackListRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun removeTrackFromHistory(track: Track)
}