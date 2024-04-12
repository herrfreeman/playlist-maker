package com.practicum.playlistmaker.medialibrary.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmaker.main.ui.MainActivity

class MediaLibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaLibraryToolbar.setNavigationOnClickListener  {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.mediaLibraryPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaLibraryTab, binding.mediaLibraryPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites_tab)
                1 -> tab.text = getString(R.string.playlists_tab)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}