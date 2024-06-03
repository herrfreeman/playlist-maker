package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Entity

@Entity(tableName = "trackinlist", primaryKeys = ["playlistid", "trackid"])
data class TrackInPlaylistEntity(
    val playlistid: Int,
    val trackid: String,
)