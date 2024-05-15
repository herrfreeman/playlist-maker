package com.practicum.playlistmaker.medialibrary.domain

import org.koin.dsl.module

val medialibraryDomainModule = module{

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(
            favoriteTracksRepository = get(),
        )
    }

}