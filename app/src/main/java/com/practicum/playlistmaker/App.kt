package com.practicum.playlistmaker
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "playlistmaker_preferences"
const val NIGHT_MODE = "NIGHT_MODE"

class App : Application() {
    lateinit var appPreferences: SharedPreferences
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        darkTheme = appPreferences.getBoolean(NIGHT_MODE, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}