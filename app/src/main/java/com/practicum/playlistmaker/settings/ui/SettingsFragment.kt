package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.core.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: SettingsViewModel by viewModel()
    private var isClickAllowed = true

    override fun createInflatedBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeAppSettings().observe(viewLifecycleOwner) {
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

}