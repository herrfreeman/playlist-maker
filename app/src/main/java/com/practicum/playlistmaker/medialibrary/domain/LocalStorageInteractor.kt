package com.practicum.playlistmaker.medialibrary.domain

import android.net.Uri

interface LocalStorageInteractor {
    suspend fun copyImageToPrivateStorage(uri: Uri): String
    suspend fun deleteImageFromPrivateStorage(fileName: String)
}