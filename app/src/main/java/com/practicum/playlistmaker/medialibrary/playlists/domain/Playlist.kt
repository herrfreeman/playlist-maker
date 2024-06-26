package com.practicum.playlistmaker.medialibrary.playlists.domain

import java.io.Serializable

data class Playlist(
    var name: String,
    var description: String,
    var coverFileName: String,
    val id: Int = 0,
    val trackCount: Int = 0,
    val totalDuration: Long = 0,
) : Serializable {
    companion object {
        const val EXTRAS_KEY = "PLAYLIST_CLASS"
    }
}