package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.main.ui.MainActivity

import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topToolbar = findViewById<MaterialToolbar>(R.id.settings_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        viewModel.observeAppSettings().observe(this) {
            binding.themeSwitcher.isChecked = it.nightMode
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNightMode(isChecked)
        }

        binding.shareApplication.setOnClickListener{viewModel.let { clickDebounce { it.shareApp() } }}
        binding.writeToSupport.setOnClickListener{viewModel.let { clickDebounce { it.openSupport() } }}
        binding.readAgreement.setOnClickListener{viewModel.let { clickDebounce { it.openTerms() } }}
    }

    private fun clickDebounce(listener: () -> Unit): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true; listener() }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}