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
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = gson.fromJson(intent.extras?.getString(Track.EXTRAS_KEY) ?: "", Track::class.java)

        val trackName = findViewById<TextView>(R.id.track_name)
        val groupName = findViewById<TextView>(R.id.group_name)

        val trackDuration = findViewById<TextView>(R.id.track_duration_value)
        val trackAlbumCaption = findViewById<TextView>(R.id.track_album_caption)
        val trackAlbum = findViewById<TextView>(R.id.track_album_value)
        val trackYear = findViewById<TextView>(R.id.track_year_value)
        val trackGenre = findViewById<TextView>(R.id.track_genre_value)
        val trackCountry = findViewById<TextView>(R.id.track_country_value)
        val trackImage = findViewById<ImageView>(R.id.track_image)
        val backButton = findViewById<ImageView>(R.id.back_button)

        trackName.text = track.trackName
        groupName.text = track.artistName
        trackDuration.text = SimpleDateFormat(
                    getString(R.string.track_duration_mask),
                    Locale.getDefault()
                ).format(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackAlbum.isVisible = trackAlbum.text.isNotEmpty()
        trackAlbumCaption.isVisible = trackAlbum.isVisible
        trackYear.text =track.releaseDate.substring(0,4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .placeholder(R.drawable.track_placeholder_236)
            .transform(RoundedCorners(2))
            .into(trackImage)

        backButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }
}