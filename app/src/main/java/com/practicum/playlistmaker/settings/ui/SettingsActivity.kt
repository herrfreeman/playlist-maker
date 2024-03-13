package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.main.ui.MainActivity

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    var viewModel: SettingsViewModel? = null
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        val topToolbar = findViewById<MaterialToolbar>(R.id.settings_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        viewModel?.observeAppSettings()?.observe(this) {
            binding.themeSwitcher.isChecked = it.nightMode
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel?.setNightMode(isChecked)
        }


        binding.shareApplication.setOnClickListener{viewModel?.let { clickDebounce { it.shareApp() } }}
        binding.writeToSupport.setOnClickListener{viewModel?.let { clickDebounce { it.openSupport() } }}
        binding.readAgreement.setOnClickListener{viewModel?.let { clickDebounce { it.openTerms() } }}
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