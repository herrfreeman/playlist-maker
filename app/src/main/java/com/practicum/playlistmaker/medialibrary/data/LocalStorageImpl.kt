package com.practicum.playlistmaker.medialibrary.data

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.practicum.playlistmaker.medialibrary.domain.LocalStorage
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class LocalStorageImpl(
    private val application: Application,
    settingsInteractor: SettingsInteractor
) : LocalStorage {

    private val appSettings = settingsInteractor.getSettings()

    override suspend fun copyImageToPrivateStorage(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val filePath = appSettings.imageDirectory!!
            //File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val fileName = "${UUID.randomUUID()}.png".lowercase()
            val file = File(filePath, fileName)
            val inputStream = application.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.PNG, 30, outputStream)
            fileName
        }

    }

    override suspend fun deleteImageFromPrivateStorage(fileName: String) {
        return withContext(Dispatchers.IO) {
            val filePath = appSettings.imageDirectory!!

            File(filePath, fileName).also {
                if (it.exists()) {
                    it.delete()
                }
            }
        }
    }

}