package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 4,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class, TrackInFavorites::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}