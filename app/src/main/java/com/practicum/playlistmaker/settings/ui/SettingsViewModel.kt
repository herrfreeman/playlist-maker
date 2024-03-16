package com.practicum.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {

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

}