package com.practicum.playlistmaker.search.data.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.api.TrackListRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackListRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
) : TrackListRepository {

     override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow{
         val response = networkClient.doRequest(TrackSearchRequest(expression))
         emit(
            when (response.resultCode) {
                -1 -> Resource.Error(context.getString(R.string.check_connection_message))
                200 -> {
                    Resource.Success((response as TrackSearchResponse).results.map {
                        TrackMapper.trackDtoToTrackMap(it)
                    })
                }
                else -> Resource.Error("${context.getString(R.string.server_error_message)} #${response.resultCode}")
            }
        )
    }

}