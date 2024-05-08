package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application, private var currentTrack: Track) : AndroidViewModel(application) {

    private var mediaPlayer = MediaPlayer()

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val progressLiveData = MutableLiveData<TrackProgress>()
    fun observeProgress(): LiveData<TrackProgress> = progressLiveData

    private var updateProgressJob: Job? = null

    init {
        setTrack(currentTrack)
        progressLiveData.postValue(TrackProgress(0L))
    }

    fun setTrack(track: Track) {
        currentTrack = track
        mediaPlayer.setDataSource(currentTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateLiveData.postValue(PlayerState.Paused(currentTrack))
        }
        mediaPlayer.setOnCompletionListener {
            stateLiveData.postValue(PlayerState.Paused(currentTrack))
            updateProgressJob?.cancel()
            progressLiveData.postValue(TrackProgress(0))
        }
    }

    fun play() {
        mediaPlayer.start()
        stateLiveData.postValue(PlayerState.Playing(currentTrack))
        updateProgressJob = infiniteUpdatingProgress()
    }

    fun pause() {
        mediaPlayer.pause()
        stateLiveData.postValue(PlayerState.Paused(currentTrack))
        updateProgressJob?.cancel()
    }

    override fun onCleared() {
        updateProgressJob?.cancel()
        mediaPlayer.release()
    }

    private fun infiniteUpdatingProgress() = viewModelScope.launch {
        while (mediaPlayer.isPlaying) {
            progressLiveData.postValue(TrackProgress(mediaPlayer.currentPosition.toLong()))
            delay(TIMER_DURATION_MILLS)
        }
    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L
    }

}