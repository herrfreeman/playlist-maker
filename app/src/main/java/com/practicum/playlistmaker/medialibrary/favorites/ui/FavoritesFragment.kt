package com.practicum.playlistmaker.medialibrary.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackSearchAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance() = FavoritesFragment()
    }

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val adapter = TrackSearchAdapter { clickDebounce { openTrack(it) } }
    private var isClickAllowed = true

    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.favoritesRecycler.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavorites()
    }

    fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> showContent(state.trackList)
            is FavoritesState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.favoritesEmptyImage.visibility = View.VISIBLE
        binding.favoritesEmptyText.visibility = View.VISIBLE
        binding.favoritesRecycler.visibility = View.GONE
    }

    private fun showContent(trackList: List<Track>) {
        binding.favoritesEmptyImage.visibility = View.GONE
        binding.favoritesEmptyText.visibility = View.GONE
        binding.favoritesRecycler.visibility = View.VISIBLE

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
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


}