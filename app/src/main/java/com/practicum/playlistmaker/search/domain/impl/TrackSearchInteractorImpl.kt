package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackSearchInteractorImpl(private val repository: TrackListRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {

        return repository.searchTracks(expression)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> resource.data to null
                    is Resource.Error -> null to resource.message
                }
            }

    }
}