package com.practicum.playlistmaker.settings.domain.models

import java.io.File

data class AppSettings(
    var nightMode: Boolean = false,
    var imageDirectory: File? = null,
)