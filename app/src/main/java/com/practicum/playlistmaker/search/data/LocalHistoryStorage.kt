package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.dto.TrackDto

class LocalHistoryStorage(private val sharedPreferences: SharedPreferences) {

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

    fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

}
