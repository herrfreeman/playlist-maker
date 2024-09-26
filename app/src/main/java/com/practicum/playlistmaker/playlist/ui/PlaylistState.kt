package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlaylistState(val playlist: Playlist) {

    class Content(
        playlist: Playlist,
        val trackList: List<Track>,
    ) : PlaylistState(playlist)

    class EmptyTracks(playlist: Playlist) : PlaylistState(playlist)

}