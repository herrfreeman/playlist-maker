package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.LocalStorageInteractor
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreatePlaylistsViewModel(
    private val storageInteractor: LocalStorageInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist?,
) : ViewModel() {

    private val editState = SingleLiveEvent<EditPlaylistState>()
    fun getEditState(): LiveData<EditPlaylistState> = editState

    private var fileName: String = ""
    private var saveFileJob: Job? = null

    fun getPlaylist(): Playlist? = playlist

    fun saveCoverFile(imageUri: Uri) {
        fileName = ""
        saveFileJob?.cancel()
        saveFileJob = viewModelScope.launch {
            fileName = storageInteractor.copyImageToPrivateStorage(imageUri)
        }
    }

    fun deleteCoverFile() {
        saveFileJob?.cancel()
        if (fileName.isNotEmpty()) {
            viewModelScope.launch {
                storageInteractor.deleteImageFromPrivateStorage(fileName)
            }
        }
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    fun savePlaylist(name: String, description: String) {
        editState.postValue(EditPlaylistState.FileLoading)
        viewModelScope.launch {
            saveFileJob?.join()

            if (playlist == null) {
                playlistInteractor.insertPlaylist(
                    Playlist(
                        name = name,
                        description = description,
                        coverFileName = fileName,
                    )
                )
                editState.postValue(EditPlaylistState.PlaylistCreated(name))
            } else {
                playlist.name = name
                playlist.description = description
                playlist.coverFileName = fileName
                playlistInteractor.insertPlaylist(playlist)
                editState.postValue(EditPlaylistState.PlaylistUpdated)
            }
        }
    }

}