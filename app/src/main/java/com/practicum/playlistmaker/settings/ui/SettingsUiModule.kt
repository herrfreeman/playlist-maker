package com.practicum.playlistmaker.settings.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsUiModule = module {
    viewModel {
        SettingsViewModel(get())
    }
}