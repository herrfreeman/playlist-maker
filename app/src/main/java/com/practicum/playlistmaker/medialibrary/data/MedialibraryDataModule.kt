package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.favorites.data.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.medialibrary.favorites.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.medialibrary.playlists.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistRepository
import org.koin.dsl.module

val medialibraryDataModule = module {

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            appDatabase = get(),
        )
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            appDatabase = get(),
        )
    }

}