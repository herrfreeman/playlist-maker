package com.practicum.playlistmaker.medialibrary.playlists.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun updatePlaylist(playlist: Playlist): Flow<Playlist>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)

}
