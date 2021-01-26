package com.ubb.david.domain

data class TrackDto(
        val id: String,
        val thumbnailUrl: String,
        val title: String,
        val artistsName: String,
        val albumId: String,
        val albumName: String,
        val spotifyUrl: String,
        val durationSeconds: Long
)