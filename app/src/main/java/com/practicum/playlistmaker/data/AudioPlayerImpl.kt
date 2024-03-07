package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.AudioPlayer

class AudioPlayerImpl: AudioPlayer {
    private val player = MediaPlayer()

    override fun currentPosition(): Int {
        return player.currentPosition
    }

    override fun prepare(sourceURL: String, preparedListener: () -> Unit, finishedListener: () -> Unit) {
        player.setDataSource(sourceURL)
        player.prepareAsync()
        player.setOnPreparedListener{preparedListener()}
        player.setOnCompletionListener {finishedListener()}
    }

    override fun play() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun destroy() {
        player.release()
    }


}