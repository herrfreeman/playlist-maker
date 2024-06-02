package com.practicum.playlistmaker.medialibrary.domain

import android.net.Uri


class LocalStorageInteractorImpl(private val localStorage: LocalStorage) : LocalStorageInteractor {

    override suspend fun copyImageToPrivateStorage(uri: Uri) =
        localStorage.copyImageToPrivateStorage(uri)

    override suspend fun deleteImageFromPrivateStorage(fileName: String) =
        localStorage.deleteImageFromPrivateStorage(fileName)

}