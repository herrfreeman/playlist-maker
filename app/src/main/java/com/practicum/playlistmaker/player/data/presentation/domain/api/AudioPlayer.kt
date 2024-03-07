package com.practicum.playlistmaker.player.data.presentation.domain.api

interface AudioPlayer {
    fun currentPosition(): Int
    fun prepare(sourceURL: String, preparedListener: () -> Unit, finishedListener: () -> Unit)
    fun play()
    fun pause()
    fun destroy()
}