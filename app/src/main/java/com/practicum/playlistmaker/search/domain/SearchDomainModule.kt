package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackSearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TrackSearchInteractorImpl
import org.koin.dsl.module

val searchDomainModule = module {

    single<TrackSearchHistoryInteractor> {
        TrackSearchHistoryInteractorImpl(
            repository = get(),
            )
    }

    single<TrackSearchInteractor> {
        TrackSearchInteractorImpl(
            repository = get(),
            )
    }

}