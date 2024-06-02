package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.medialibrary.favorites.ui.FavoritesViewModel
import com.practicum.playlistmaker.medialibrary.playlists.ui.CreatePlaylistsViewModel
import com.practicum.playlistmaker.medialibrary.playlists.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val medialibraryUiModel = module {

    viewModel {
        FavoritesViewModel(
            favoritesInteractor = get(),
        )
    }

    viewModel {
        PlaylistsViewModel(
            playlistInteractor = get(),
        )
    }

    viewModel {
        CreatePlaylistsViewModel(
            storageInteractor = get(),
            playlistInteractor = get(),
        )
    }
}