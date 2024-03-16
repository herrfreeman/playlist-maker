package com.practicum.playlistmaker.settings.data

import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import org.koin.dsl.module

val settingsDataModule = module {

    single {
        LocalSettingsStorage(
            context = get(),
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            localStorage = get(),
        )
    }



}