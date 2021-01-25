package com.ubb.david.playlistcreator.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubb.david.playlistcreator.R
import com.ubb.david.playlistcreator.data.Authenticator
import com.ubb.david.playlistcreator.databinding.FragmentMainBinding
import com.ubb.david.playlistcreator.domain.TrackDto

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var tracksListAdapter: TracksListAdapter
    private val viewModel: MainViewModel = MainViewModel()
    private var currentTracks = listOf<TrackDto>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tracksListAdapter = TracksListAdapter(
            onOpenExternalClicked = { url -> openExternalUrl(url) },
            onDeleteClicked = { trackDto -> viewModel.deleteTrack(trackDto.id) }
        )
        binding = FragmentMainBinding.inflate(inflater).also { it.init() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tracks.observe(viewLifecycleOwner) { tracks -> onNewTracksData(tracks) }
        viewModel.errors.observe(viewLifecycleOwner) { errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show() }
    }

    private fun onNewTracksData(tracks: List<TrackDto>) {
        when {
            currentTracks.isEmpty() -> tracksListAdapter.items = tracks
            currentTracks.size < tracks.size -> tracksListAdapter.addTracks(tracks)
            else -> currentTracks.indexOfFirst { track -> track !in tracks }.takeIf { it != -1 }
                ?.let { trackIndex -> tracksListAdapter.removeTrack(trackIndex) }
        }
    }

    private fun openExternalUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun FragmentMainBinding.init() {
        logoutButton.setOnClickListener {
            Authenticator.logout()
            findNavController().navigate(MainFragmentDirections.actionToStartup())
        }
        welcomeLabel.text = getString(R.string.lbl_welcome_user, Authenticator.user?.displayName)
        newPlaylistButton.setOnClickListener {
            newPlaylistButton.isVisible = false
            viewModel.createNewPlaylist()
        }
        tracksList.adapter = tracksListAdapter
        tracksList.layoutManager = LinearLayoutManager(requireContext())
    }
}
