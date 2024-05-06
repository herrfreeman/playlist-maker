package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TrackSearchHistoryInteractorImpl(private val repository: TrackSearchHistoryRepository) : TrackSearchHistoryInteractor {

    override fun getSearchHistory(): Flow<List<Track>> = repository.getSearchHistory()

    override fun addToHistory(track: Track): Flow<List<Track>> = repository.addTrackToHistory(track)

    override fun clearHistory(): Flow<List<Track>> = repository.clearHistory()

}