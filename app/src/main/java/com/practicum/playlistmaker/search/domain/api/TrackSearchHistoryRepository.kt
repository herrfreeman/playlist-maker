package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackSearchHistoryRepository {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getSearchHistory(): List<Track>
}