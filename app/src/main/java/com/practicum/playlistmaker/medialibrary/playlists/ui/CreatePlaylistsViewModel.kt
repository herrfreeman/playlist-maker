package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.LocalStorageInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreatePlaylistsViewModel(
    private val storageInteractor: LocalStorageInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val createState = MutableLiveData<CreatePlaylistState>()
    fun getCreateState(): LiveData<CreatePlaylistState> = createState

    private var localFileName: String = ""
    private var saveFileJob: Job? = null

    fun saveCoverFile(imageUri: Uri) {
        localFileName = ""
        saveFileJob = viewModelScope.launch {
            localFileName = storageInteractor.copyImageToPrivateStorage(imageUri)
        }
    }

    fun deleteCoverFile() {
        saveFileJob?.cancel()
        if (localFileName.isNotEmpty()) {
            viewModelScope.launch {
                storageInteractor.deleteImageFromPrivateStorage(localFileName)
            }
        }
    }

    fun createPlaylist(name: String, description: String) {

        createState.postValue(CreatePlaylistState.FileLoading)

        viewModelScope.launch {
            saveFileJob?.join()
            playlistInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    coverFileName = localFileName
                )
            )
            createState.postValue(CreatePlaylistState.PlaylistCreated)
        }
    }

}