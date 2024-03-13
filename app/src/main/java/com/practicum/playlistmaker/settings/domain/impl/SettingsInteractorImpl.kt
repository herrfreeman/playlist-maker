package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun getSettings(): AppSettings = repository.getSettings()
    override fun saveSettings(settings: AppSettings) = repository.setSettings(settings)

}