package com.practicum.playlistmaker.medialibrary.playlists.domain

import android.app.Application
import com.practicum.playlistmaker.R

class TrackCountString(application: Application) {

    private val str0 = application.getString(R.string.track_count_0)
    private val str1 = application.getString(R.string.track_count_1)
    private val str2_4 = application.getString(R.string.track_count_2_4)
    private val str5_20 = application.getString(R.string.track_count_5_20)


    fun convert(count: Int): String {
        return if(count in 5..19)  {
            str5_20
        } else {
            strByLastDigit(count % 10)
        }
    }

    private fun strByLastDigit(lastDigit: Int): String {
        return when(lastDigit) {
            0 -> str0
            1 -> str1
            2,3,4 -> str2_4
            else -> str5_20
        }
    }
}