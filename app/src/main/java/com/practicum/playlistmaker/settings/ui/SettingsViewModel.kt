package com.practicum.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.utils.Creator
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class SettingsViewModel(private val application: Application) : AndroidViewModel(application) {
    private val sharingInteractor: SharingInteractor = Creator.provideSharingInteractor(getApplication<Application>())
    private val settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor(getApplication<Application>())

    private val appSettingsLiveData = MutableLiveData<AppSettings>()
    fun observeAppSettings(): LiveData<AppSettings> = appSettingsLiveData

    init {
        appSettingsLiveData.postValue((application as PlayListApplication).appSettings)
    }

    fun setNightMode(nightMode: Boolean) {
        val app = (application as PlayListApplication)
        if (nightMode != app.appSettings.nightMode) {
            app.appSettings.nightMode = nightMode
            settingsInteractor.saveSettings(app.appSettings)
            app.updateTheme()
            appSettingsLiveData.postValue(app.appSettings)
        }
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}