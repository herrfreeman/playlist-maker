package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(application: Application, private var currentTrack: Track) : AndroidViewModel(application) {

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val progressLiveData = MutableLiveData<TrackProgress>()
    fun observeProgress(): LiveData<TrackProgress> = progressLiveData

    private val infiniteUpdateProgress = object : Runnable {
        override fun run() {
            progressLiveData.postValue(TrackProgress(mediaPlayer.currentPosition.toLong()))
            handler.postDelayed(this, TIMER_DURATION_MILLS)
        }
    }

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
            handler.removeCallbacks(infiniteUpdateProgress)
            progressLiveData.postValue(TrackProgress(0))
        }
    }

    fun play() {
        mediaPlayer.start()
        stateLiveData.postValue(PlayerState.Playing(currentTrack))
        handler.post(infiniteUpdateProgress)
    }

    fun pause() {
        mediaPlayer.pause()
        stateLiveData.postValue(PlayerState.Paused(currentTrack))
        handler.removeCallbacks(infiniteUpdateProgress)
    }

    override fun onCleared() {
        handler.removeCallbacks(infiniteUpdateProgress)
        mediaPlayer.release()
    }

    companion object {
        private const val TIMER_DURATION_MILLS = 300L

//        fun getViewModelFactory(startTrack: Track): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application, startTrack)
//            }
//        }
    }

}