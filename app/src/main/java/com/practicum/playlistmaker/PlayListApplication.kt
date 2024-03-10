package com.practicum.playlistmaker
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class PlayListApplication : Application() {

    lateinit var appSettings: AppSettings

    override fun onCreate() {
        super.onCreate()
        val settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor(this)
        appSettings = settingsInteractor.getSettings()
        updateTheme()
    }

    fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (appSettings.nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                //AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

}