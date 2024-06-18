package com.practicum.playlistmaker.medialibrary.favorites.data

import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.search.data.mapper.toEntity
import com.practicum.playlistmaker.search.data.mapper.toTrack
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistRepository: PlaylistRepository,
) : FavoriteTracksRepository {

    override suspend fun insertTrack(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.appDao().insertTrack(track.toEntity().apply {
                isFavorite = true
                favoriteTimestamp = System.currentTimeMillis() / 1000
            })
        }
    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.appDao().insertTrack(track.toEntity().apply {
                isFavorite = false
                favoriteTimestamp = 0L
            })
            playlistRepository.clearAloneTracks()
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = withContext(Dispatchers.IO) {
            appDatabase.appDao().getFavoriteTracks()
        }
        emit(tracks.map { it.toTrack() })
    }

    override fun getFavoriteTracksId(): Flow<List<String>> = flow {
        val idList = withContext(Dispatchers.IO) {
            appDatabase.appDao().getFavoriteTracksId()
        }
        emit(idList)
    }

}