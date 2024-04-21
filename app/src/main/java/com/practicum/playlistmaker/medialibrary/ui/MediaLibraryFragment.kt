package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {

    private lateinit var tabMediator: TabLayoutMediator

    override fun createInflatedBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaLibraryBinding {
        return FragmentMediaLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaLibraryPager.adapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaLibraryTab, binding.mediaLibraryPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites_tab)
                1 -> tab.text = getString(R.string.playlists_tab)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

}