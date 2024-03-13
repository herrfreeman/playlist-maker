package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackListRepository) : TrackSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackSearchInteractor.TrackSearchConsumer) {
        executor.execute {
            val resource = repository.searchTracks(expression)
            when (resource) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}