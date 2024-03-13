package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.AppSettings

interface SettingsInteractor {
    fun getSettings(): AppSettings
    fun saveSettings(settings: AppSettings)
}