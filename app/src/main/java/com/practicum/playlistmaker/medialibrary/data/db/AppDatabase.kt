package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [TrackEntity::class, PlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}