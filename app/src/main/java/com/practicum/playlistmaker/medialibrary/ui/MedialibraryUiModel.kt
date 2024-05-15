package com.practicum.playlistmaker.medialibrary.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val medialibraryUiModel = module {

    viewModel {
        FavoritesViewModel(
            favoritesInteractor = get(),
        )
    }

    viewModel {
        PlaylistsViewModel()
    }

}