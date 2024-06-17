package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 6,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    fun clearAloneTracks() {
        val aloneTracks = appDao().getAloneTracks()
        for (track in aloneTracks) {
            appDao().deleteTrack(track)
        }
    }
}

