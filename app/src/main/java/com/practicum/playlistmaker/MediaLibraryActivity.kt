package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.appbar.MaterialToolbar

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        val topToolbar = findViewById<MaterialToolbar>(R.id.mediaLibraryToolbar)
        topToolbar.setNavigationOnClickListener  {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}