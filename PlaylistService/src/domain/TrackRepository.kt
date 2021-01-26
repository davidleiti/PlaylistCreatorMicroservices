package com.ubb.david.domain

import com.ubb.david.data.Track

class TrackRepository {

    private val _localTracks: MutableList<Track> = mutableListOf()
    val localTracks: List<Track>
        get() = _localTracks

    fun addTracks(tracks: List<Track>): Boolean = _localTracks.addAll(tracks)

    fun removeTrack(trackId: String): Boolean = _localTracks.removeIf { track -> track.id == trackId}
}