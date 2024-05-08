package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    suspend fun searchSong(
        @Query("term") text: String
    ): TrackSearchResponse

}