package com.practicum.playlistmaker.search.data

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.dto.TrackDto

class LocalHistoryStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"

    }

    fun saveSearchHistory(searchHistory: List<TrackDto>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, gson.toJson(searchHistory))
            .apply()
    }

    fun getSearchHistory(): List<TrackDto> {
        return gson.fromJson(
            sharedPreferences.getString(SEARCH_HISTORY, ""),
            Array<TrackDto>::class.java
        )?.asList() ?: emptyList<TrackDto>()
    }

    fun clearHistory(): List<TrackDto> {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
        return emptyList<TrackDto>()
    }

}
