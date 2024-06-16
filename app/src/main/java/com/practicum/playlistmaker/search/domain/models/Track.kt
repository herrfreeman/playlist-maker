package com.practicum.playlistmaker.search.domain.models

import java.io.Serializable

data class Track(
    val id: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val trackTimeMillis: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean,

) : Serializable {

    companion object {
        const val EXTRAS_KEY = "TRACK_CLASS"

        fun getEmpty() = Track("","","","","",0L,"","","","","", false)
    }


}
