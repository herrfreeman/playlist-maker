package com.practicum.playlistmaker

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String

) {
    companion object {
        const val EXTRAS_KEY = "TRACK_CLASS"
    }
}
