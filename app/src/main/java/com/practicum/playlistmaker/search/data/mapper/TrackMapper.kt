package com.practicum.playlistmaker.search.data.mapper

import com.practicum.playlistmaker.medialibrary.data.db.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track

object TrackMapper {
    fun trackDtoToTrackMap(trackDto: TrackDto) = Track(
        id = trackDto.id,
        trackName = trackDto.trackName,
        artistName = trackDto.artistName,
        artworkUrl100 = trackDto.artworkUrl100,
        artworkUrl512 = trackDto.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
        trackTimeMillis = (trackDto.trackTimeMillis.toLongOrNull() ?: 0L),
        collectionName = trackDto.collectionName,
        releaseDate = trackDto.releaseDate,
        primaryGenreName = trackDto.primaryGenreName,
        country = trackDto.country,
        previewUrl = trackDto.previewUrl,
    )

    fun trackToTrackDtoMap(track: Track) = TrackDto(
        id = track.id,
        trackName = track.trackName,
        artistName = track.artistName,
        artworkUrl100 = track.artworkUrl100,
        trackTimeMillis = track.trackTimeMillis.toString(),
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        previewUrl = track.previewUrl,
    )

    fun trackEntityToTrackMap(trackEntity: TrackEntity) = Track(
        id = trackEntity.id,
        trackName = trackEntity.trackName,
        artistName = trackEntity.artistName,
        artworkUrl100 = trackEntity.artworkUrl100,
        artworkUrl512 = trackEntity.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
        trackTimeMillis = (trackEntity.trackTimeMillis.toLongOrNull() ?: 0L),
        collectionName = trackEntity.collectionName,
        releaseDate = trackEntity.releaseDate,
        primaryGenreName = trackEntity.primaryGenreName,
        country = trackEntity.country,
        previewUrl = trackEntity.previewUrl,
    )

    fun trackToTrackEntityMap(track: Track) = TrackEntity(
        id = track.id,
        trackName = track.trackName,
        artistName = track.artistName,
        artworkUrl100 = track.artworkUrl100,
        trackTimeMillis = track.trackTimeMillis.toString(),
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        previewUrl = track.previewUrl,
        timestamp = 0L,
    )
}
