package com.ubb.david.domain

import com.ubb.david.data.Track
import java.util.*

class TrackRepository {

    private val _localTracks: MutableMap<String, MutableSet<Track>> = mutableMapOf()

    fun getTracks(playlistId: String): Set<Track>? = _localTracks[playlistId]

    fun createPlaylist(tracks: List<Track>): String {
        val playlistUuid = UUID.randomUUID().toString()
        _localTracks[playlistUuid] = tracks.toMutableSet()
        return playlistUuid
    }

    fun addTracks(playlistId: String, tracks: List<Track>): Boolean {
        println("Adding the following data: $tracks")
        return _localTracks[playlistId]?.addAll(tracks) ?: false
    }

    fun removeTrack(playlistId: String, trackId: String): Boolean =
        _localTracks[playlistId]?.removeIf { track -> track.id == trackId } ?: false
}