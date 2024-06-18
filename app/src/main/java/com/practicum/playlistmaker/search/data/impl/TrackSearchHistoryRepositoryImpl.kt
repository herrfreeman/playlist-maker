package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.data.LocalHistoryStorage
import com.practicum.playlistmaker.search.data.mapper.toDto
import com.practicum.playlistmaker.search.data.mapper.toTrack
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class TrackSearchHistoryRepositoryImpl(
    private val localStorage: LocalHistoryStorage,
    private val searchHistorySize: Int,
    private val favoriteTracksRepository: FavoriteTracksRepository,
) : TrackSearchHistoryRepository {

    override fun addTrackToHistory(track: Track): Flow<List<Track>> = flow {

        val currentHistory = localStorage.getSearchHistory()
            .map { it.toTrack() }
            .filter { it.id != track.id }
            .toMutableList()

        currentHistory.add(0, track)

        localStorage.saveSearchHistory(currentHistory
            .subList(0, listOf(currentHistory.size, searchHistorySize).min())
            .map { it.toDto() })

        val historyWithFavorites = applyFavorites(currentHistory)
        emit(historyWithFavorites)
    }

    override fun clearHistory(): Flow<List<Track>> = flow {
        emit(
            localStorage.clearHistory()
                .map { it.toTrack() }
        )
    }

    override fun getSearchHistory(): Flow<List<Track>> = flow {
        val currentHistory = applyFavorites(
            localStorage.getSearchHistory()
                .map { it.toTrack() }
        )

        emit(
            currentHistory
                .subList(0, listOf(currentHistory.size, searchHistorySize).min())
        )
    }

    private suspend fun applyFavorites(tracks: List<Track>): List<Track> {
        val favorites = favoriteTracksRepository.getFavoriteTracksId().toList()

        return tracks.map { track ->
            track.apply {
                favorites.forEach { favorite -> if (favorite.contains(id)) isFavorite = true }
            }
        }
    }
}
