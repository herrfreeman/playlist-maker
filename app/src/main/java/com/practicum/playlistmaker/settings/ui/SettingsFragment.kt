package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.core.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

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
            clickDebounce { viewModel.setNightMode(isChecked) }
        }

        binding.shareApplication.setOnClickListener{ clickDebounce { viewModel.shareApp() } }
        binding.writeToSupport.setOnClickListener{ clickDebounce { viewModel.openSupport() } }
        binding.readAgreement.setOnClickListener{ clickDebounce { viewModel.openTerms() } }
    }

    private fun clickDebounce(listener: () -> Unit) {
        if (isClickAllowed) {
            isClickAllowed = false
            listener()
            lifecycleScope.launch { isClickAllowed = true; delay(CLICK_DEBOUNCE_DELAY) }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

}