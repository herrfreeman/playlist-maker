package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepository {

    override suspend fun insertTrack(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.favoriteTracksDao().insertTrack(
                TrackMapper.trackToTrackEntityMap(track)
                    .apply { timestamp = System.currentTimeMillis()/1000 }
            )
        }
    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            appDatabase.favoriteTracksDao().deleteTrack(TrackMapper.trackToTrackEntityMap(track))
        }
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = withContext(Dispatchers.IO) {
            appDatabase.favoriteTracksDao().getTracks()
        }
        emit(tracks.map {TrackMapper.trackEntityToTrackMap(it)})
    }

    override fun getTracksId(): Flow<List<String>> = flow {
        val idList = withContext(Dispatchers.IO) {
            appDatabase.favoriteTracksDao().getTracksId()
        }
        emit(idList)
    }

}