package com.practicum.playlistmaker.medialibrary.playlists.domain

import android.app.Application
import com.practicum.playlistmaker.R

class TrackCountString(application: Application) {

    private val count_str0 = application.getString(R.string.track_count_0)
    private val count_str1 = application.getString(R.string.track_count_1)
    private val count_str2_4 = application.getString(R.string.track_count_2_4)
    private val count_str5_20 = application.getString(R.string.track_count_5_20)

    private val duration_str0 = application.getString(R.string.track_duration_0)
    private val duration_str1 = application.getString(R.string.track_duration_1)
    private val duration_str2_4 = application.getString(R.string.track_duration_2_4)
    private val duration_str5_20 = application.getString(R.string.track_duration_5_20)

    fun convertCount(count: Int): String {
        return if(count in 5..19)  {
            count_str5_20
        } else {
            count_StrByLastDigit(count % 10)
        }
    }

    private fun count_StrByLastDigit(lastDigit: Int): String {
        return when(lastDigit) {
            0 -> count_str0
            1 -> count_str1
            2,3,4 -> count_str2_4
            else -> count_str5_20
        }
    }

    fun convertDuration(count: Int): String {
        return if(count in 5..19)  {
            duration_str5_20
        } else {
            duration_StrByLastDigit(count % 10)
        }
    }

    private fun duration_StrByLastDigit(lastDigit: Int): String {
        return when(lastDigit) {
            0 -> duration_str0
            1 -> duration_str1
            2,3,4 -> duration_str2_4
            else -> duration_str5_20
        }
    }
}