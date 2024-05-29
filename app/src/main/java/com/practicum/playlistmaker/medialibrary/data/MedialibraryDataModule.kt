package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.domain.FavoriteTracksRepository
import org.koin.dsl.module

val medialibraryDataModule = module {

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            appDatabase = get(),
        )
    }
}