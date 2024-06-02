package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.os.Bundle
import android.os.Environment
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialibrary.favorites.ui.FavoritesState
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackSearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListsFragment()
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = PlaylistRecyclerAdapter {  }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {render(it)}
        binding.playlistRecycler.adapter = adapter
        binding.playlistRecycler.layoutManager = GridLayoutManager(context, 2)

        binding.createPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
        }

    }


    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylists()
    }

    fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.mediaImageNoplaylists.isVisible = true
        binding.noplaylistText.isVisible = true
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.mediaImageNoplaylists.isVisible = false
        binding.noplaylistText.isVisible = false

        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}