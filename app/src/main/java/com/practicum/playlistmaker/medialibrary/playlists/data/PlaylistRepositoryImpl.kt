package com.practicum.playlistmaker.medialibrary.playlists.data

import com.practicum.playlistmaker.medialibrary.data.PlaylistMapper
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.TrackInPlaylistEntity
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            appDatabase.appDao().insertPlaylist(
                PlaylistMapper.playlistToPlaylistEntity(playlist)
            )
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = withContext(Dispatchers.IO) {
            appDatabase.appDao().getPlaylists()
        }
        emit(playlists)
    }

    override suspend fun addTrackToPlaytist(playlist: Playlist, track: Track): Boolean {
        return withContext(Dispatchers.IO) {
            if (appDatabase.appDao().checkTrackInPlaylist(track.id, playlist.id) == 0) {
                appDatabase.appDao().insertTrackInPlaylist(
                    TrackInPlaylistEntity(
                        playlistid = playlist.id,
                        trackid = track.id,
                    )
                )
                false
            } else {
                true
            }
        }
    }
}