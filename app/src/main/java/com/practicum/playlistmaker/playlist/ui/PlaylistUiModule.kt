package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistUiModule = module {
    viewModel { (currentPlaylist: Playlist) ->
        PlaylistViewModel(
            application = get(),
            currentPlaylist = currentPlaylist,
            playlistInteractor = get(),
            sharingInteractor = get(),
            trackCounter = get(),
        )
    }
}