package com.practicum.playlistmaker

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long
)
