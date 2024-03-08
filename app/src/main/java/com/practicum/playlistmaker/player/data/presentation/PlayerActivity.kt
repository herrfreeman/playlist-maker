package com.practicum.playlistmaker.player.data.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.TrackSearchActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.data.presentation.domain.api.AudioPlayer
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private var mediaPlayer: AudioPlayer = Creator.providePlayer()
    private var playerState = STATE_DEFAULT
    private var handler: Handler? = null
    private val runUpdateCounter = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                setTimer(mediaPlayer.currentPosition())
                handler?.postDelayed(this, TIMER_DURATION_MILLS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        track = intent.extras?.getSerializable(Track.EXTRAS_KEY, Track::class.java) ?: Track("","","","",0L,"","","","","",)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            trackName.text = track.trackName
            groupName.text = track.artistName
            trackDurationValue.text = SimpleDateFormat(
                getString(R.string.track_duration_mask),
                Locale.getDefault()
            ).format(track.trackTimeMillis)
            trackAlbumValue.text = track.collectionName
            trackAlbumValue.isVisible = trackAlbumValue.text.isNotEmpty()
            trackAlbumCaption.isVisible = trackAlbumValue.isVisible
            trackYearValue.text = track.releaseDate.substring(0, 4)
            trackGenreValue.text = track.primaryGenreName
            trackCountryValue.text = track.country

            Glide.with(this@PlayerActivity)
                .load(track.artworkUrl512)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder_236)
                .transform(RoundedCorners(16))
                .into(trackImage)

            backButton.setOnClickListener {
                startActivity(Intent(this@PlayerActivity, TrackSearchActivity::class.java))
            }

            mediaPlayer.prepare(track.previewUrl,
                {
                    setPlayButtonVisibility(true)
                    playerState = STATE_PREPARED
                }, {
                    setPlayButtonVisibility(true)
                    playerState = STATE_PREPARED
                    handler?.removeCallbacks(runUpdateCounter)
                    setTimer(0)
                })

            binding.trackPlayButton.setOnClickListener { playPlayer() }
            binding.trackPauseButton.setOnClickListener { pausePlayer() }

        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.destroy()
        handler?.removeCallbacks(runUpdateCounter)
        handler = null
    }

    private fun setPlayButtonVisibility(isVisible: Boolean) {
        with(binding) {
            trackPlayButton.isVisible = isVisible
            trackPauseButton.isVisible = !trackPlayButton.isVisible
        }
    }

    private fun playPlayer() {
        mediaPlayer.play()
        playerState = STATE_PLAYING
        setPlayButtonVisibility(false)
        handler?.post(runUpdateCounter)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        setPlayButtonVisibility(true)
        handler?.removeCallbacks(runUpdateCounter)
    }

    private fun setTimer(timerPosition: Int) {
        binding.playTimer.text = SimpleDateFormat(
            getString(R.string.track_duration_mask),
            Locale.getDefault()
        ).format(timerPosition)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_DURATION_MILLS = 300L
    }
}