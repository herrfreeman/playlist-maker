package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Search button clicked!", Toast.LENGTH_SHORT).show()
        }

        val mediaButton = findViewById<Button>(R.id.media_button)
        val clickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Media button clicked!", Toast.LENGTH_LONG).show()
            }
        }
        mediaButton.setOnClickListener(clickListener)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Settings button clicked!", Toast.LENGTH_LONG).show()
            }
        })

    }


}