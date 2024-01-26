package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        track = intent.extras?.getSerializable(Track.EXTRAS_KEY, Track::class.java) ?: Track()
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
                startActivity(Intent(this@PlayerActivity, SearchActivity::class.java))
            }
        }
    }
}