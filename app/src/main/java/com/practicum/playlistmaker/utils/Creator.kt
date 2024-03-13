package com.practicum.playlistmaker.utils

import android.content.Context
import com.practicum.playlistmaker.search.data.impl.TrackListRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.LocalHistoryStorage
import com.practicum.playlistmaker.search.data.impl.TrackSearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackSearchHistoryRepository
import com.practicum.playlistmaker.search.domain.impl.TrackSearchHistoryInteractorImpl
import com.practicum.playlistmaker.settings.data.LocalSettingsStorage
import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.settings.domain.impl.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SharingInteractorImpl

object Creator {


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
            LocalHistoryStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)),
            searchHistorySize = 5,
        )
    }

    fun provideTrackSearchHistoryInteractor(context: Context): TrackSearchHistoryInteractor {
        return TrackSearchHistoryInteractorImpl(getTrackSearchHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(LocalSettingsStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)))
    }

    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(ExternalNavigator(context), context)
    }
}