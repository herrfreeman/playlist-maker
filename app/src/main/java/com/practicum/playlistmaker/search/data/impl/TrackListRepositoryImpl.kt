package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.LocalStorage
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource

class TrackListRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
) : TrackListRepository {

     override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")
            200 -> {
                //val stored = localStorage.getSavedFavorites()

                return Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        id = it.id,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        artworkUrl100 = it.artworkUrl100,
                        trackTimeMillis = 0L,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl,
                        //stored.contains(it.id)
                    )
                })
            }
            else -> return Resource.Error("Ошибка сервера")
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track.id)
    }

    override fun removeTrackFromHistory(track: Track) {
        localStorage.removeFromHistory(track.id)
    }
}