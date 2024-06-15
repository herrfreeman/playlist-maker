package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trackinfavorites")
data class TrackInFavorites(
    @PrimaryKey
    val trackid: String,
    var timestamp: Long,
)