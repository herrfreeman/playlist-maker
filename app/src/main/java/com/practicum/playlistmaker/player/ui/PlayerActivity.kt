package com.practicum.playlistmaker.player.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.TrackSearchActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress

import java.text.SimpleDateFormat
import java.util.Locale

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(intent.extras?.getSerializable(Track.EXTRAS_KEY, Track::class.java) ?: Track.getEmpty())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)

        viewModel.observeState().observe(this) {renderTrackState(it)}
        viewModel.observeProgress().observe(this) {renderTrackProgress(it)}

        with(binding) {
            setContentView(root)

            backButton.setOnClickListener {
                startActivity(Intent(this@PlayerActivity, TrackSearchActivity::class.java))
                viewModel.pause()
            }

            trackPlayButton.setOnClickListener { viewModel.play() }
            trackPauseButton.setOnClickListener { viewModel.pause() }

        }
    }

    fun renderTrackState(playerState: PlayerState) {
        when(playerState) {
            is PlayerState.Paused -> {
                setPlayButtonVisibility(true)
                showTrack(playerState.track)
            }
            is PlayerState.Playing -> {
                setPlayButtonVisibility(false)
                showTrack(playerState.track)
            }
        }
    }

    fun renderTrackProgress(trackProgress: TrackProgress) {
        binding.playTimer.text = SimpleDateFormat(
            getString(R.string.track_duration_mask),
            Locale.getDefault()
        ).format(trackProgress.currentPositionMills)
    }

    fun showTrack(track: Track) {
        with(binding) {
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

        }
    }

    private fun setPlayButtonVisibility(isVisible: Boolean) {
        with(binding) {
            trackPlayButton.isVisible = isVisible
            trackPauseButton.isVisible = !trackPlayButton.isVisible
        }
    }

}