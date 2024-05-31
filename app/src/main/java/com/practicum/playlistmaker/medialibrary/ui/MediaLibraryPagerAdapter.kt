package com.practicum.playlistmaker.medialibrary.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.medialibrary.favorites.ui.FavoritesFragment
import com.practicum.playlistmaker.medialibrary.playlists.ui.PlayListsFragment


class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesFragment.newInstance()
        else PlayListsFragment.newInstance()
    }
}