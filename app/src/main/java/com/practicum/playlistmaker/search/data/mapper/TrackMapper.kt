package com.practicum.playlistmaker.search.data.mapper

import com.practicum.playlistmaker.medialibrary.data.db.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track

fun TrackDto.toTrack() = Track(
    id = id,
    trackName = trackName,
    artistName = artistName,
    artworkUrl100 = artworkUrl100,
    artworkUrl512 = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
    trackTimeMillis = (trackTimeMillis.toIntOrNull() ?: 0),
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    isFavorite = false,
)

fun Track.toDto() = TrackDto(
    id = id,
    trackName = trackName,
    artistName = artistName,
    artworkUrl100 = artworkUrl100,
    trackTimeMillis = trackTimeMillis.toString(),
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
)

fun TrackEntity.toTrack() = Track(
    id = id,
    trackName = trackName,
    artistName = artistName,
    artworkUrl100 = artworkUrl100,
    artworkUrl512 = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
    trackTimeMillis = trackTimeMillis,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    isFavorite = isFavorite,
)

fun Track.toEntity() = TrackEntity(
    id = id,
    trackName = trackName,
    artistName = artistName,
    artworkUrl100 = artworkUrl100,
    trackTimeMillis = trackTimeMillis,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    isFavorite = isFavorite,
    favoriteTimestamp = 0L,
)

