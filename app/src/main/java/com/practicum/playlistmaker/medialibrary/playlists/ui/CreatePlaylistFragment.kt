package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CreatePlaylistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!
    private lateinit var textWatcher: TextWatcher
    private val viewModel: CreatePlaylistsViewModel by viewModel()
    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

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
                s: kotlin.CharSequence?,
                start: kotlin.Int,
                count: kotlin.Int,
                after: kotlin.Int
            ) {
            }

            override fun onTextChanged(
                s: kotlin.CharSequence?,
                start: kotlin.Int,
                before: kotlin.Int,
                count: kotlin.Int
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
                } else {
                    Log.d("PLAYER_DEBUG", "No media selected")
                    // TODO Сделать как надо в задании
                }
            }

        binding.playlistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.nameField.text.toString(),
                binding.descriptionField.text.toString(),
            )

        }

        confirmExitDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_exit_title)
            .setMessage(R.string.playlist_exit_text)
            .setPositiveButton(R.string.cancel_button, null)
            .setNegativeButton(R.string.finish_button) { dialog, which ->
                findNavController().popBackStack()
            }

        binding.topAppBar.setNavigationOnClickListener {
            exitWithoutSaving()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            exitWithoutSaving()
        }
    }

    private fun render(it: CreatePlaylistState) {
        when (it) {
            CreatePlaylistState.FileLoaded -> TODO()
            CreatePlaylistState.FileLoading -> binding.progressBar.isVisible = true
            CreatePlaylistState.Nothing -> TODO()
            CreatePlaylistState.PlaylistCreated -> findNavController().popBackStack()
        }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreatePlaylistFragment.
         */
        // TODO: Rename and change types and number of parameters
//        fun newInstance(param1: String, param2: String) =
//            CreatePlaylistFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}