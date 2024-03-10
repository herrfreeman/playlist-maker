package com.practicum.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.MediaLibraryActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.TrackSearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            startActivity(Intent(this, TrackSearchActivity::class.java))
        }

        val mediaButton = findViewById<Button>(R.id.media_button)
        mediaButton.setOnClickListener {
            startActivity(Intent(this, MediaLibraryActivity::class.java))
        }

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

}