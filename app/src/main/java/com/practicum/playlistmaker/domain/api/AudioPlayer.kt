package com.practicum.playlistmaker.domain.api

interface AudioPlayer {
    fun currentPosition(): Int
    fun prepare(sourceURL: String, preparedListener: () -> Unit, finishedListener: () -> Unit)
    fun play()
    fun pause()
    fun destroy()
}