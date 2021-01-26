package com.ubb.david.playlistcreator.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
    private val mainViewModel: MainViewModel = MainViewModel()
    private var currentTracks = listOf<TrackDto>()

    private var currentPlaylistId: String?
        get() = context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)?.getString(KEY_PLAYLIST_ID, "").takeIf { !it.isNullOrEmpty() }
        set(value) {
            context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)?.edit()?.putString(KEY_PLAYLIST_ID, value)?.apply()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tracksListAdapter = TracksListAdapter(
            onAddClicked = { trackDto -> currentPlaylistId?.let { playlistId -> mainViewModel.addAlbumTracks(playlistId, trackDto) } },
            onOpenExternalClicked = { url -> openExternalUrl(url) },
            onDeleteClicked = { trackDto ->
                currentPlaylistId?.let { playlistId ->
                    mainViewModel.deleteTrack(playlistId, trackDto.id)
                }
            }
        )
        binding = FragmentMainBinding.inflate(inflater).also { it.init() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.tracks.observe(viewLifecycleOwner) { tracks -> onNewTracksData(tracks) }
        mainViewModel.errors.observe(viewLifecycleOwner) { errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show() }
    }

    private fun onNewTracksData(tracks: List<TrackDto>) {
        when {
            currentTracks.isEmpty() -> tracksListAdapter.items = tracks
            currentTracks.size < tracks.size -> tracksListAdapter.addTracks(tracks)
            tracks.size - currentTracks.size > 1 -> tracksListAdapter.items = tracks
            else -> currentTracks.indexOfFirst { track -> track !in tracks }.takeIf { it != -1 }
                ?.let { trackIndex -> tracksListAdapter.removeTrack(trackIndex) }
        }
    }

    private fun openExternalUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun FragmentMainBinding.init() {
        lifecycleOwner = viewLifecycleOwner
        viewModel = mainViewModel
        executePendingBindings()
        logoutButton.setOnClickListener {
            Authenticator.logout()
            findNavController().navigate(MainFragmentDirections.actionToStartup())
        }
        welcomeLabel.text = getString(R.string.lbl_welcome_user, Authenticator.user?.displayName)
        newPlaylistButton.setOnClickListener {
            mainViewModel.createNewPlaylist { playlistDto -> currentPlaylistId = playlistDto.playlistId }
        }
        tracksList.adapter = tracksListAdapter
        tracksList.layoutManager = LinearLayoutManager(requireContext())
        loadPreviousPlaylist.isVisible = currentPlaylistId != null
        loadPreviousPlaylist.setOnClickListener { currentPlaylistId?.let { playlistId -> mainViewModel.fetchPlaylistTracks(playlistId) } }
    }

    companion object {
        private const val SHARED_PREFS_NAME = "PlaylistCreatorSharedPrefs"
        private const val KEY_PLAYLIST_ID = "KEY_PLAYLIST_ID"
    }
}
