package com.practicum.playlistmaker.search.data

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalHistoryStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"

    }

    suspend fun saveSearchHistory(searchHistory: List<TrackDto>) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, gson.toJson(searchHistory))
            .apply()
    }

    suspend fun getSearchHistory(): List<TrackDto> = withContext(Dispatchers.IO) {
        gson.fromJson(
            sharedPreferences.getString(SEARCH_HISTORY, ""),
            Array<TrackDto>::class.java
        )?.asList() ?: emptyList<TrackDto>()
    }

    suspend fun clearHistory(): List<TrackDto> = withContext(Dispatchers.IO) {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
        emptyList<TrackDto>()
    }

}
