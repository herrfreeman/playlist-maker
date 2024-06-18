package com.practicum.playlistmaker.medialibrary.favorites.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    fun getFavoriteTracksId(): Flow<List<String>>

}
