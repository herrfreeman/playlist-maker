package com.practicum.playlistmaker
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.player.ui.playerUiModule
import com.practicum.playlistmaker.search.data.searchDataModule
import com.practicum.playlistmaker.search.domain.searchDomainModule
import com.practicum.playlistmaker.search.ui.searchUiModule
import com.practicum.playlistmaker.settings.data.settingsDataModule
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings
import com.practicum.playlistmaker.settings.domain.settingsDomainModule
import com.practicum.playlistmaker.settings.ui.settingsUiModule
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class PlayListApplication : Application() {

    lateinit var appSettings: AppSettings
    val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlayListApplication)
            modules(
                playerUiModule,
                searchDataModule,
                searchDomainModule,
                searchUiModule,
                settingsDataModule,
                settingsDomainModule,
                settingsUiModule,
            )
        }

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