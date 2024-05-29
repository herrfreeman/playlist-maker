package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoriteTracksInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun updateFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        stateLiveData.postValue(FavoritesState.Empty)
                    } else {
                        stateLiveData.postValue(FavoritesState.Content(tracks.onEach { it.isFavorite = true }))
                    }
                }
        }
    }
}