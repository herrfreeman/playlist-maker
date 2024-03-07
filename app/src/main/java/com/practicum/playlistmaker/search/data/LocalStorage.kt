package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences

class LocalStorage(private val sharedPreferences: SharedPreferences) {

    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    fun addToHistory(trackId: String) {
        changeFavorites(trackId = trackId, remove = false)
    }

    fun removeFromHistory(trackId: String) {
        changeFavorites(trackId = trackId, remove = true)
    }

    fun getSearchHistory(): Set<String> {
        return sharedPreferences.getStringSet(SEARCH_HISTORY, emptySet()) ?: emptySet()
    }

    private fun changeFavorites(trackId: String, remove: Boolean) {
        val mutableSet = getSearchHistory().toMutableSet()
        val modified = if (remove) mutableSet.remove(trackId) else mutableSet.add(trackId)
        if (modified) sharedPreferences.edit().putStringSet(SEARCH_HISTORY, mutableSet).apply()
    }

}