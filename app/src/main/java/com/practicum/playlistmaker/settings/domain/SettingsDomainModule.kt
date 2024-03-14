package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.settings.domain.impl.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val settingsDomainModule = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single {
        ExternalNavigator(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }
}