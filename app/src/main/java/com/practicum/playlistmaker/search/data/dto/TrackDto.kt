package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class TrackDto (
    @SerializedName("trackId") val id: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
)