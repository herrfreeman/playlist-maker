package com.practicum.playlistmaker.player.ui.models

sealed interface PlayerState {

    object Playing: PlayerState
    object Paused : PlayerState

}