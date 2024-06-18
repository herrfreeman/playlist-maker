package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!
    private lateinit var textWatcher: TextWatcher
    private val viewModel: CreatePlaylistsViewModel by viewModel {
        parametersOf(arguments?.getSerializable(Playlist.EXTRAS_KEY, Playlist::class.java))
    }
    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder
    private var isClickAllowed = true
    private var imageSelected = false

    private val settingsInteractor: SettingsInteractor by inject()
    private val appSettings = settingsInteractor.getSettings()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylist()?.let {
            binding.nameField.setText(it.name)
            binding.descriptionField.setText(it.description)
            binding.saveButton.setText(getString(R.string.playlist_save_button_caption))
            binding.saveButton.isEnabled = true
            binding.topAppBar.title = getString(R.string.edit_playlist_title)
            viewModel.setFileName(it.coverFileName)
            if (it.coverFileName.isNotEmpty() && appSettings.imageDirectory != null) {
                binding.playlistImage.setImageURI(
                    File(appSettings.imageDirectory, it.coverFileName).toUri()
                )
            }
        }

        viewModel.getEditState().observe(viewLifecycleOwner) {
            render(it)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                binding.saveButton.isEnabled = !s.toString().isEmpty()
            }
        }

        binding.nameField.addTextChangedListener(textWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageSelected = true
                    binding.playlistImage.setImageURI(uri)
                    viewModel.saveCoverFile(uri)
                }
            }

        binding.playlistImage.setOnClickListener {
            clickDebounce {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.saveButton.setOnClickListener {
            clickDebounce {
                viewModel.savePlaylist(
                    binding.nameField.text.toString(),
                    binding.descriptionField.text.toString(),
                )
            }
        }

        confirmExitDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_exit_title)
            .setMessage(R.string.playlist_exit_text)
            .setPositiveButton(R.string.cancel_button, null)
            .setNegativeButton(R.string.finish_button) { dialog, which ->
                findNavController().popBackStack()
            }

        binding.topAppBar.setNavigationOnClickListener {
            clickDebounce {
                exitWithoutSaving()
            }
        }

        requireActivity().apply {
            onBackPressedDispatcher.addCallback {
                exitWithoutSaving()
            }
        }
    }

    private fun render(it: EditPlaylistState) {
        when (it) {
            is EditPlaylistState.FileLoading -> binding.progressBar.isVisible = true
            is EditPlaylistState.PlaylistCreated -> {
                showToast(getString(R.string.playlist_created).format(it.playlistName))
                findNavController().popBackStack()
            }

            is EditPlaylistState.PlaylistUpdated -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun exitWithoutSaving() {
        if (viewModel.getPlaylist() == null) {
            if (binding.nameField.text.isNullOrEmpty()
                and binding.descriptionField.text.isNullOrEmpty()
                and !imageSelected
            ) findNavController().popBackStack()
            else confirmExitDialog.show()
            viewModel.deleteCoverFile()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.saveButton.removeTextChangedListener(textWatcher)
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
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(Playlist.EXTRAS_KEY to playlist)
    }
}