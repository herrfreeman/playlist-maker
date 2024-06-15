package com.practicum.playlistmaker.medialibrary.playlists.data

import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.TrackInPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.toEntity
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.search.data.mapper.toEntity
import com.practicum.playlistmaker.search.data.mapper.toTrack
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
            appDatabase.appDao().insertPlaylist(playlist.toEntity())
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
                appDatabase.appDao().insertTrack(track.toEntity())
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

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        val trackList = withContext(Dispatchers.IO) {
            appDatabase.appDao().getTracksInPlaylist(playlist.id)
        }
        emit(trackList.map { it.toTrack() })
    }
}