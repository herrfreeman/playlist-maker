package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val topToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
        topToolbar.setNavigationOnClickListener  {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}