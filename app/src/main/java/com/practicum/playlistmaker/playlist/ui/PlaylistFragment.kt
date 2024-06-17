package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.TrackCountString
import com.practicum.playlistmaker.medialibrary.playlists.ui.CreatePlaylistFragment
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackSearchAdapter
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import kotlin.math.round

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(Playlist.EXTRAS_KEY, Playlist::class.java))
    }
    private val settingsInteractor: SettingsInteractor by inject()
    private val appSettings = settingsInteractor.getSettings()
    private val trackCounter: TrackCountString by inject()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var bottomSheetState = BottomSheetBehavior.STATE_HIDDEN
    private var overlayAlpha = 1.0F
    private var isClickAllowed = true

    private val adapter = TrackSearchAdapter(
        trackClickListener = { clickDebounce { openTrack(it) } },
        trackLongClickListener = { clickDebounce { askDeleteTrack(it) } }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeToastState().observe(viewLifecycleOwner) { showToast(it) }
        viewModel.observePlaylist().observe(viewLifecycleOwner) { renderPlaylist(it) }

        binding.playlistRecycler.adapter = adapter
        renderPlaylist(
            requireArguments().getSerializable(
                Playlist.EXTRAS_KEY,
                Playlist::class.java
            )!!
        )
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.shareButton.setOnClickListener { askSharePlaylist() }
        binding.shareBottomButton.setOnClickListener { askSharePlaylist() }
        binding.menuButton.setOnClickListener {
            clickDebounce {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        binding.deletePlaylistButton.setOnClickListener { askDeletePlaylist() }
        binding.editPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_createPlaylistFragment,
                CreatePlaylistFragment.createArgs(viewModel.getPlatlist())
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }
        binding.overlay.setOnClickListener {
            clickDebounce { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.isVisible = (newState != BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetState = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0) {
                    binding.overlay.alpha = 0.6F + slideOffset * 0.4F
                } else {
                    binding.overlay.alpha = 0.6F + slideOffset * 0.6F
                }
                overlayAlpha = binding.overlay.alpha
            }
        })

        bottomSheetBehavior.state = bottomSheetState
        binding.overlay.isVisible = (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
        binding.overlay.alpha = overlayAlpha


    }


    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylist()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.trackList)
            is PlaylistState.Empty -> showEmpty()
        }
    }

    private fun renderPlaylist(playlist: Playlist) {
        val totalDuration = round(playlist.totalDuration / 60000.0).toInt()
        val trackCount = playlist.trackCount
        val countString = trackCounter.convertCount(playlist.trackCount)
        val durationString = trackCounter.convertDuration(totalDuration)

        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistDuration.text = "$totalDuration $durationString"
        binding.playlistTrackCount.text = "$trackCount $countString"

        binding.bottomPlaylistName.text = playlist.name
        binding.bottomPlaylistTrackCount.text = "$trackCount $countString"


        val imageDirectory = appSettings.imageDirectory
        if (playlist.coverFileName.isNotEmpty() and (imageDirectory != null)) {
            val file = File(imageDirectory, playlist.coverFileName)
            if (file.exists()) {
                //playlistImage.setImageURI(file.toUri()) //Works too slow with full image files
                Glide.with(requireContext())
                    .load(file.toUri())
                    .centerCrop()
                    .placeholder(R.drawable.playlist_placeholder)
                    //.transform(RoundedCorners(2))
                    .into(binding.coverImage)

                Glide.with(requireContext())
                    .load(file.toUri())
                    .centerCrop()
                    .placeholder(R.drawable.playlist_placeholder)
                    //.transform(RoundedCorners(2))
                    .into(binding.bottomPlaylistImage)

            }
        } else {
            binding.coverImage.setImageResource(R.drawable.playlist_placeholder)
            binding.bottomPlaylistImage.setImageResource(R.drawable.track_placeholder_45)
        }

    }

    @Suppress("notifyDataSetChanged")
    private fun showEmpty() {
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    @Suppress("notifyDataSetChanged")
    private fun showContent(trackList: List<Track>) {
        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun clickDebounce(listener: () -> Unit) {
        if (isClickAllowed) {
            isClickAllowed = false
            listener()
            lifecycleScope.launch { delay(CLICK_DEBOUNCE_DELAY); isClickAllowed = true }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun askDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_track_dialog)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ -> viewModel.deleteTrack(track) }
            .show()
    }

    private fun askSharePlaylist() {
        if (adapter.trackList.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.no_track_dialog)
                .setNeutralButton(R.string.ok) { _, _ -> }
                .show()
        } else {
            viewModel.sharePlaylist(adapter.trackList)
        }
    }

    private fun askDeletePlaylist() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_playlist_dialog).format(viewModel.getPlatlist().name))
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteCurrentPlaylist()
                findNavController().popBackStack()
            }
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(Playlist.EXTRAS_KEY to playlist)
    }

}

