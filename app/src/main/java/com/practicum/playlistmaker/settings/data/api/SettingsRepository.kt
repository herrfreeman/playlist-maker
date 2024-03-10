package com.practicum.playlistmaker.settings.data.api

import com.practicum.playlistmaker.settings.domain.models.AppSettings

interface SettingsRepository {
    fun getSettings(): AppSettings
    fun setSettings(settings: AppSettings)
}