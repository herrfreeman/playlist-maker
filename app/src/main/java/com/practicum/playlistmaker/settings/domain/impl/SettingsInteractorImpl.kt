package com.practicum.playlistmaker.settings.domain.impl

import android.content.Context
import android.os.Environment
import com.practicum.playlistmaker.settings.data.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings

class SettingsInteractorImpl(context: Context, private val repository: SettingsRepository) :
    SettingsInteractor {

    val appSettings: AppSettings = repository.getSettings().apply {
        imageDirectory = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
    }

    override fun getSettings(): AppSettings = appSettings
    override fun saveSettings() = repository.setSettings(appSettings.copy(imageDirectory = null))

}