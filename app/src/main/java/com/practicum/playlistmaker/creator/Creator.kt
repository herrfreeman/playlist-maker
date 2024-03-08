package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.player.data.presentation.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.data.presentation.data.AudioPlayerImpl
import com.practicum.playlistmaker.search.data.impl.TrackListRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackSearchInteractorImpl

import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.LocalStorage
import com.practicum.playlistmaker.search.data.impl.TrackSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.impl.TrackSearchHistoryInteractorImpl

object Creator {

    fun providePlayer() : AudioPlayer {
        return AudioPlayerImpl()
    }

    private fun getTrackListRepository(context: Context): TrackListRepository {
        return TrackListRepositoryImpl(
            RetrofitNetworkClient(context),
        )
    }

    fun provideTrackSearchInteractor(context: Context): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackListRepository(context))
    }

    private fun getTrackSearchHistoryRepository(context: Context): TrackSearchHistoryRepository {
        return TrackSearchHistoryRepositoryImpl(
            LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)),
            searchHistorySize = 5,
        )
    }

    fun provideTrackSearchHistoryInteractor(context: Context): TrackSearchHistoryInteractor {
        return TrackSearchHistoryInteractorImpl(getTrackSearchHistoryRepository(context))
    }

}