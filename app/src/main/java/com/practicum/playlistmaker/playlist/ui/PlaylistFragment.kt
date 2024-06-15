package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackSearchAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getSerializable(Playlist.EXTRAS_KEY, Playlist::class.java))
    }

    private var isClickAllowed = true

    private val adapter = TrackSearchAdapter { clickDebounce { openTrack(it) } }


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

        val playlist: Playlist = requireArguments().getSerializable(Playlist.EXTRAS_KEY, Playlist::class.java)!!
        with(binding) {
            playlistRecycler.adapter = adapter

            playlistName.text = playlist.name
            playlistDescription.text = playlist.description
            //playlistDuration.text = TODO()
            playlistTrackCount.text = "${playlist.trackCount} tracks" //TODO

            val imageDirectory = (requireActivity().application as PlayListApplication).imageDirectory
            if (playlist.coverFileName.isNotEmpty() and (imageDirectory != null)) {
                val file = File(imageDirectory, playlist.coverFileName)
                if (file.exists()) {
                    //playlistImage.setImageURI(file.toUri()) //Works too slow with full image files
                    Glide.with(requireContext())
                        .load(file.toUri())
                        .centerCrop()
                        .placeholder(R.drawable.playlist_placeholder)
                        //.transform(RoundedCorners(2))
                        .into(coverImage)
                }
            } else {
                coverImage.setImageResource(R.drawable.playlist_placeholder)
            }


            topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

        }


        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }
    }


    override fun onResume() {
        super.onResume()
        //viewModel.updatePlaylists()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.trackList)
            is PlaylistState.Empty -> showEmpty()
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

//    private fun renderPlaylists(playlists: List<Playlist>) {
//        adapter.playlists.clear()
//        adapter.playlists.addAll(playlists)
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun renderTrackState(playerState: PlayerState) {
//        when (playerState) {
//            is PlayerState.Paused -> {
//                setPlayButtonVisibility(true)
//                //showTrack(playerState.track)
//            }
//
//            is PlayerState.Playing -> {
//                setPlayButtonVisibility(false)
//                //showTrack(playerState.track)
//            }
//        }
//    }
//
//    private fun renderTrackProgress(trackProgress: TrackProgress) {
//        binding.playTimer.text = SimpleDateFormat(
//            getString(R.string.track_duration_mask),
//            Locale.getDefault()
//        ).format(trackProgress.currentPositionMills)
//    }
//
//    private fun showTrack(track: Track) {
//        with(binding) {
//            trackName.text = track.trackName
//            groupName.text = track.artistName
//            trackDurationValue.text = SimpleDateFormat(
//                getString(R.string.track_duration_mask),
//                Locale.getDefault()
//            ).format(track.trackTimeMillis)
//            trackAlbumValue.text = track.collectionName
//            trackAlbumValue.isVisible = trackAlbumValue.text.isNotEmpty()
//            trackAlbumCaption.isVisible = trackAlbumValue.isVisible
//            trackYearValue.text = track.releaseDate.substring(0, 4)
//            trackGenreValue.text = track.primaryGenreName
//            trackCountryValue.text = track.country
//            if (track.isFavorite) {
//                trackLikeButton.setImageResource(R.drawable.unlike_track_button)
//            } else {
//                trackLikeButton.setImageResource(R.drawable.like_track_button)
//            }
//
//            Glide.with(this@PlaylistFragment)
//                .load(track.artworkUrl512)
//                .centerCrop()
//                .placeholder(R.drawable.track_placeholder_236)
//                .transform(RoundedCorners(16))
//                .into(trackImage)
//
//        }
//    }
//


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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(Playlist.EXTRAS_KEY to playlist)
    }

}

