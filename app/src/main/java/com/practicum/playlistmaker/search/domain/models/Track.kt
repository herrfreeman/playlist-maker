package com.practicum.playlistmaker.search.domain.models

import java.io.Serializable

data class Track(
    val id: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,

) : Serializable {
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    companion object {
        const val EXTRAS_KEY = "TRACK_CLASS"
    }
}