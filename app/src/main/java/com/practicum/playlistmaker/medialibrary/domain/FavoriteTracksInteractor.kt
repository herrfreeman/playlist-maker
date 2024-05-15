package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getTracks(): Flow<List<Track>>

    fun getTracksId(): Flow<List<String>>

}