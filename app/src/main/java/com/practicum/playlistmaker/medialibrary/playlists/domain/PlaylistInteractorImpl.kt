package com.practicum.playlistmaker.medialibrary.playlists.domain

import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) =
        playlistRepository.insertPlaylist(playlist)

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()

}