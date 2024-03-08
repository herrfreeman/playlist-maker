package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackSearchHistoryInteractor {

    fun getSearchHistory(consumer: HistoryChangeConsumer)
    fun addToHistory(track: Track, consumer: HistoryChangeConsumer)
    fun clearHistory(consumer: HistoryChangeConsumer)

    interface HistoryChangeConsumer {
        fun consume(tracks: List<Track>?)
    }
}