package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface PlayerState {

    data class Playing(val track: Track) : PlayerState
    data class Paused(val track: Track) : PlayerState

}