package com.practicum.playlistmaker.medialibrary.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmaker.main.ui.MainActivity

class MediaLibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaLibraryToolbar.setNavigationOnClickListener  {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}