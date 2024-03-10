package com.practicum.playlistmaker.settings.data

import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class SettingsRepositoryImpl(private val localStorage: LocalSettingsStorage,) : SettingsRepository {

    override fun getSettings(): AppSettings = localStorage.getSettings()
    override fun setSettings(settings: AppSettings) = localStorage.setSettings(settings)

}