package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.LocalHistoryStorage
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchHistoryRepositoryImpl(
    private val localStorage: LocalHistoryStorage,
    private val searchHistorySize: Int,
) : TrackSearchHistoryRepository {

    override fun addTrackToHistory(track: Track): Flow<List<Track>> = flow {

        val currentHistory = localStorage.getSearchHistory()
            .map { TrackMapper.trackDtoToTrackMap(it) }
            .filter { it.id != track.id }
            .toMutableList()

        currentHistory.add(0, track)

        localStorage.saveSearchHistory(currentHistory
            .subList(0, listOf(currentHistory.size, searchHistorySize).min() )
            .map { TrackMapper.trackToTrackDtoMap(it) })

        emit(currentHistory)
    }

    override fun clearHistory(): Flow<List<Track>> = flow {
        emit(
            localStorage.clearHistory()
                .map { TrackMapper.trackDtoToTrackMap(it) }
        )
    }

    override fun getSearchHistory(): Flow<List<Track>> = flow {
        val currentHistory = localStorage.getSearchHistory()
        emit(
            currentHistory
            .subList(0, listOf(currentHistory.size, searchHistorySize).min() )
            .map { TrackMapper.trackDtoToTrackMap(it) }
        )
    }

}
