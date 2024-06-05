package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = PlaylistRecyclerAdapter { }

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.playlistRecycler.adapter = adapter
        binding.playlistRecycler.layoutManager = GridLayoutManager(context, 2)

        binding.createPlaylist.setOnClickListener {
            clickDebounce {
                findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
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

    private fun clickDebounce(listener: () -> Unit) {
        if (isClickAllowed) {
            isClickAllowed = false
            listener()
            lifecycleScope.launch { delay(CLICK_DEBOUNCE_DELAY); isClickAllowed = true }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance() = PlayListsFragment()
    }

}