package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}