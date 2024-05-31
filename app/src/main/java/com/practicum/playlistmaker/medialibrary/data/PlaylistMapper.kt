package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist

object PlaylistMapper {

    fun playlistToPlaylistEntity(playlist: Playlist) = PlaylistEntity(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        coverFileName = playlist.coverFileName,
    )

    fun playlistEntityToPlaylist(playlistEntity: PlaylistEntity) = Playlist(
        id = playlistEntity.id,
        name = playlistEntity.name,
        description = playlistEntity.description,
        coverFileName = playlistEntity.coverFileName,
    )
}