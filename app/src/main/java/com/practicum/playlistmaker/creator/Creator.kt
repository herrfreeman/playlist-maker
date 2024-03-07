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

object Creator {

    fun providePlayer() : AudioPlayer {
        return AudioPlayerImpl()
    }

    private fun getTrackListRepository(context: Context): TrackListRepository {
        return TrackListRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)),
        )
    }

    fun provideTrackSearchInteractor(context: Context): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackListRepository(context))
    }

}