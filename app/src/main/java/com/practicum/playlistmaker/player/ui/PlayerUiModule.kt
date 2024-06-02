package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerUiModule = module {
    viewModel {(currentTrack: Track) ->
        PlayerViewModel(
            application = get(),
            currentTrack = currentTrack,
            favoritesInteractor = get(),
            playlistInteractor = get(),
        )
    }
}