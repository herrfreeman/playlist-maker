package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchHistoryRepository {
    fun addTrackToHistory(track: Track): Flow<List<Track>>
    fun clearHistory(): Flow<List<Track>>
    fun getSearchHistory(): Flow<List<Track>>
}