package com.practicum.playlistmaker.medialibrary.favorites.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    override suspend fun insertTrack(track: Track) = favoriteTracksRepository.insertTrack(track)

    override suspend fun deleteTrack(track: Track) = favoriteTracksRepository.deleteTrack(track)

    override fun getTracks(): Flow<List<Track>> = favoriteTracksRepository.getTracks()

    override fun getTracksId(): Flow<List<String>> = favoriteTracksRepository.getTracksId()

}