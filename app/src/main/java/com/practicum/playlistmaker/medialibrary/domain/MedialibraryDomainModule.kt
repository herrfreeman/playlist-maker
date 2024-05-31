package com.practicum.playlistmaker.medialibrary.domain

import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksInteractor
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractorImpl
import org.koin.dsl.module

val medialibraryDomainModule = module{

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(
            favoriteTracksRepository = get(),
        )
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            playlistRepository = get(),
        )
    }

}