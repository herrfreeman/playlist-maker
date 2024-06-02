package com.practicum.playlistmaker.medialibrary.data

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.medialibrary.domain.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class LocalStorageImpl(private val application: Application) : LocalStorage {

    override suspend fun copyImageToPrivateStorage(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val filePath = (application as PlayListApplication).imageDirectory!!
            //File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val fileName = "${UUID.randomUUID()}.png".lowercase()
            val file = File(filePath, fileName)
            val inputStream =
                (application as PlayListApplication).contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.PNG, 30, outputStream)
            Log.d("PLAYER_DEBUG", "create file $filePath / $fileName")
            fileName
        }

    }

    override suspend fun deleteImageFromPrivateStorage(fileName: String) {
        return withContext(Dispatchers.IO) {
            val filePath = (application as PlayListApplication).imageDirectory!!

            File(filePath, fileName).also {
                if (it.exists()) {
                    it.delete()
                    Log.d("PLAYER_DEBUG", "delete file $filePath / $fileName")
                }
            }
        }
    }

}