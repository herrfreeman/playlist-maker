package com.practicum.playlistmaker.medialibrary.playlists.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class Playlist(
    val name: String,
    val description: String,
    val coverFileName: String,
    val id: Int = 0,
    val trackCount: Int = 0,
)