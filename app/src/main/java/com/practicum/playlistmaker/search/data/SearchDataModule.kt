package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.impl.TrackListRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TrackSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import org.koin.dsl.module

val searchDataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = get(),
        )
    }

    single<TrackListRepository> {
        TrackListRepositoryImpl(
            networkClient = get(),
        )
    }

    single {
        LocalHistoryStorage(
            context = get(),
        )
    }

    single<TrackSearchHistoryRepository> {
        TrackSearchHistoryRepositoryImpl(
            localStorage = get(),
            searchHistorySize = 10,
            )
    }
}