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

    val appSettings = settingsInteractor.getSettings()

    private val appSettingsLiveData = MutableLiveData<AppSettings>()
    fun observeAppSettings(): LiveData<AppSettings> = appSettingsLiveData

    init {
        appSettingsLiveData.postValue(appSettings)
    }

    fun setNightMode(nightMode: Boolean) {
        appSettings.nightMode = nightMode
        settingsInteractor.saveSettings()
        appSettingsLiveData.postValue(appSettings)
        (application as PlayListApplication).updateTheme()
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()

}