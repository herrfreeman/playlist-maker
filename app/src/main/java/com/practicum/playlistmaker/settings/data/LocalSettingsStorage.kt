package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class LocalSettingsStorage(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    private companion object {
        const val APP_SETTINGS = "APP_SETTINGS"

    }

    fun getSettings(): AppSettings {
        val storedSettings: AppSettings? = gson.fromJson(
            sharedPreferences.getString(APP_SETTINGS, ""),
            AppSettings::class.java
        )
        return storedSettings ?: AppSettings()
    }

    fun setSettings(settings: AppSettings) {
        sharedPreferences
            .edit()
            .putString(APP_SETTINGS, gson.toJson(settings))
            .apply()
    }

}