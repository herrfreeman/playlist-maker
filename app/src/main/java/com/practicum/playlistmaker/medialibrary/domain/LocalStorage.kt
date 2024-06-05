package com.practicum.playlistmaker.medialibrary.domain

import android.net.Uri

interface LocalStorage {
    suspend fun copyImageToPrivateStorage(uri: Uri): String
    suspend fun deleteImageFromPrivateStorage(fileName: String)
}