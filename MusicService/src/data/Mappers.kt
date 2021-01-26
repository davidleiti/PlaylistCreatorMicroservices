package com.ubb.david.data

import com.ubb.david.domain.Track
import com.ubb.david.domain.TrackDto
import domain.AlbumItem
import java.util.concurrent.TimeUnit

fun Track.toTrackDto(): TrackDto = TrackDto(
    id = id,
    thumbnailUrl = album.images.firstOrNull()?.url.orEmpty(),
    title = name,
    artistsName = artists.firstOrNull()?.name.orEmpty(),
    albumId = album.id,
    albumName = album.name,
    spotifyUrl = externalUrls.spotify,
    durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMs.toLong())
)

fun AlbumItem.toTrackDto(): TrackDto = TrackDto(
    id = id,
    thumbnailUrl = "",
    title = name,
    artistsName = artists.firstOrNull()?.name.orEmpty(),
    albumId = "",
    albumName = "",
    spotifyUrl = externalUrls.spotify,
    durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMs.toLong())
)