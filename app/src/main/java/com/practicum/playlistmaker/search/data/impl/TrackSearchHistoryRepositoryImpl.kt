package com.practicum.playlistmaker.search.data.impl

import android.hardware.camera2.params.LensShadingMap
import com.practicum.playlistmaker.search.data.LocalStorage
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource

class TrackSearchHistoryRepositoryImpl(
    private val localStorage: LocalStorage,
    private val searchHistorySize: Int,
) : TrackSearchHistoryRepository {

    override fun addTrackToHistory(track: Track) {
        val historyTrackList = getSearchHistory().toMutableList()
        historyTrackList.removeIf { it.id == track.id }
        historyTrackList.add(0, track)
        localStorage.saveSearchHistory(historyTrackList
            .subList(0, listOf(historyTrackList.size, searchHistorySize).min() )
            .toList()
            .map { TrackMapper.trackToTrackDtoMap(it) })
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getSearchHistory(): List<Track> {
        return localStorage.getSearchHistory()
            .subList(0, listOf(localStorage.getSearchHistory().size, searchHistorySize).min() )
            .map { TrackMapper.trackDtoToTrackMap(it) }
    }

}
