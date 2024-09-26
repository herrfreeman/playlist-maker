package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist

fun Playlist.toEntity() = PlaylistEntity(
    id = id,
    name = name,
    description = description,
    coverFileName = coverFileName,
)

fun PlaylistEntity.toPlaylist() = Playlist(
    id = id,
    name = name,
    description = description,
    coverFileName = coverFileName,
)
