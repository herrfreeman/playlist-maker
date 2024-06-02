package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.player.ui.models.PlayerState
import com.practicum.playlistmaker.player.ui.models.TrackProgress
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!

    private var isClickAllowed = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val adapter = BottomPlaylistRecyclerAdapter { }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(Track.EXTRAS_KEY, Track::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            playlistsBottomRecycler.adapter = adapter

            topAppBar.setNavigationOnClickListener {
                viewModel.pause()
                findNavController().popBackStack()
            }

            trackPlayButton.setOnClickListener { clickDebounce { viewModel.play() } }
            trackPauseButton.setOnClickListener { clickDebounce { viewModel.pause() } }
            trackLikeButton.setOnClickListener { clickDebounce { viewModel.likeUnlike() } }
            trackAddButton.setOnClickListener {
                clickDebounce {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            createPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            }

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    overlay.isVisible = (newState != BottomSheetBehavior.STATE_HIDDEN)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset >= 0) {
                        overlay.alpha = 0.6F + slideOffset * 0.4F
                    } else {
                        overlay.alpha = 0.6F + slideOffset * 0.6F
                    }
                }
            })
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

        viewModel.observeState().observe(viewLifecycleOwner) { renderTrackState(it) }
        viewModel.observeProgress().observe(viewLifecycleOwner) { renderTrackProgress(it) }
        viewModel.observeTrack().observe(viewLifecycleOwner) { showTrack(it) }
        viewModel.observePlaylists().observe(viewLifecycleOwner) { renderPlaylists(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylists()
    }

    private fun renderPlaylists(playlists: List<Playlist>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun renderTrackState(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Paused -> {
                setPlayButtonVisibility(true)
                //showTrack(playerState.track)
            }

            is PlayerState.Playing -> {
                setPlayButtonVisibility(false)
                //showTrack(playerState.track)
            }
        }
    }

    private fun renderTrackProgress(trackProgress: TrackProgress) {
        binding.playTimer.text = SimpleDateFormat(
            getString(R.string.track_duration_mask),
            Locale.getDefault()
        ).format(trackProgress.currentPositionMills)
    }

    private fun showTrack(track: Track) {
        with(binding) {
            trackName.text = track.trackName
            groupName.text = track.artistName
            trackDurationValue.text = SimpleDateFormat(
                getString(R.string.track_duration_mask),
                Locale.getDefault()
            ).format(track.trackTimeMillis)
            trackAlbumValue.text = track.collectionName
            trackAlbumValue.isVisible = trackAlbumValue.text.isNotEmpty()
            trackAlbumCaption.isVisible = trackAlbumValue.isVisible
            trackYearValue.text = track.releaseDate.substring(0, 4)
            trackGenreValue.text = track.primaryGenreName
            trackCountryValue.text = track.country
            if (track.isFavorite) {
                trackLikeButton.setImageResource(R.drawable.unlike_track_button)
            } else {
                trackLikeButton.setImageResource(R.drawable.like_track_button)
            }

            Glide.with(this@PlayerFragment)
                .load(track.artworkUrl512)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder_236)
                .transform(RoundedCorners(16))
                .into(trackImage)

        }
    }

    private fun setPlayButtonVisibility(isVisible: Boolean) {
        with(binding) {
            trackPlayButton.isVisible = isVisible
            trackPauseButton.isVisible = !trackPlayButton.isVisible
        }
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
        fun createArgs(track: Track): Bundle =
            bundleOf(Track.EXTRAS_KEY to track)
    }

}