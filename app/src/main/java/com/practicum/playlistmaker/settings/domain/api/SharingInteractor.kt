package com.practicum.playlistmaker.settings.domain.api

interface SharingInteractor {
    fun shareApp()
    fun shareText(text: String)
    fun openTerms()
    fun openSupport()
}