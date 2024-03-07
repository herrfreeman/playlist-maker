package com.practicum.playlistmaker

import com.practicum.playlistmaker.domain.api.AudioPlayer
import com.practicum.playlistmaker.data.AudioPlayerImpl

object Creator {

    fun providePlayer() : AudioPlayer {
        return AudioPlayerImpl()
    }
}