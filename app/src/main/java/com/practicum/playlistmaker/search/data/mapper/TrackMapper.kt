package com.practicum.playlistmaker.search.data.mapper

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
}
