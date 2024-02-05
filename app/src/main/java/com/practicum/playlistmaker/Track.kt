package com.practicum.playlistmaker

import java.io.Serializable

data class Track(
    val trackId: Int = 0,
    val trackName: String = "",
    val artistName: String = "",
    val artworkUrl100: String = "",
    val trackTimeMillis: Long = 0L,
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String = "",
    val country: String = ""

) : Serializable {
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    companion object {
        const val EXTRAS_KEY = "TRACK_CLASS"
    }
}
