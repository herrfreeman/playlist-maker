package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class TrackSearchHistoryInteractorImpl(private val repository: TrackSearchHistoryRepository) : TrackSearchHistoryInteractor {

    override fun getSearchHistory(consumer: TrackSearchHistoryInteractor.HistoryChangeConsumer) {
        val t = Thread {
            consumer.consume(repository.getSearchHistory())
        }
        t.start()
    }

    override fun addToHistory(
        track: Track,
        consumer: TrackSearchHistoryInteractor.HistoryChangeConsumer
    ) {
        val t = Thread {
            repository.addTrackToHistory(track)
            consumer.consume(repository.getSearchHistory())
        }
        t.start()
    }

    override fun clearHistory(consumer: TrackSearchHistoryInteractor.HistoryChangeConsumer) {
        val t = Thread {
            repository.clearHistory()
        }
        t.start()
        consumer.consume(emptyList<Track>())
    }
}