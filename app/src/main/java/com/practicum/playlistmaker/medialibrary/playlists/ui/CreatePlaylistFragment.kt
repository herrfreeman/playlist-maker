package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!
    private lateinit var textWatcher: TextWatcher
    private val viewModel: CreatePlaylistsViewModel by viewModel()
    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder
    private var imageUri: Uri? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreatePlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCreateState().observe(viewLifecycleOwner) {
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
                binding.createButton.isEnabled = !s.toString().isEmpty()
            }
        }

        binding.nameField.addTextChangedListener(textWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistImage.setImageURI(uri)
                    viewModel.saveCoverFile(uri)
                }
            }

        binding.playlistImage.setOnClickListener {
            clickDebounce {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.createButton.setOnClickListener {
            clickDebounce {
                viewModel.createPlaylist(
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

    private fun render(it: CreatePlaylistState) {
        when (it) {
            is CreatePlaylistState.FileLoading -> binding.progressBar.isVisible = true
            is CreatePlaylistState.PlaylistCreated -> {
                showToast(getString(R.string.playlist_created).format(it.playlistName))
                findNavController().popBackStack()
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }


    private fun exitWithoutSaving() {
        if (binding.nameField.text.isNullOrEmpty()
            and binding.descriptionField.text.isNullOrEmpty()
            and (imageUri == null)
        ) findNavController().popBackStack()
        else confirmExitDialog.show()
        viewModel.deleteCoverFile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.createButton.removeTextChangedListener(textWatcher)
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
    }
}