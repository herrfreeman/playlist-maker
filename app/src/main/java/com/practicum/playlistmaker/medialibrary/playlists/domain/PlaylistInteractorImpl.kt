package com.practicum.playlistmaker.medialibrary.playlists.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) =
        playlistRepository.insertPlaylist(playlist)

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()

    override suspend fun addTrackToPlaytist(playlist: Playlist, track: Track) =
        playlistRepository.addTrackToPlaytist(playlist, track)

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> =
        playlistRepository.getTracksInPlaylist(playlist)

}