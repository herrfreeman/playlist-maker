package com.practicum.playlistmaker.search.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchUiModule = module {
    viewModel {
        TrackSearchViewModel(get())
    }
}