package com.practicum.playlistmaker.medialibrary.playlists.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) =
        playlistRepository.insertPlaylist(playlist)

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()

    override fun updatePlaylist(playlist: Playlist): Flow<Playlist> = playlistRepository.updatePlaylist(playlist)

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) =
        playlistRepository.addTrackToPlaylist(playlist, track)

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> =
        playlistRepository.getTracksInPlaylist(playlist)

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) =
        playlistRepository.deleteTrackFromPlaylist(track, playlist)

    override suspend fun deletePlaylist(playlist: Playlist) =
        playlistRepository.deletePlaylist(playlist)

}