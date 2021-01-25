package com.ubb.david.playlistcreator.domain

data class TrackDto(
    val id: String,
    val title: String,
    val artistsName: String,
    val albumName: String,
    val albumId: String,
    val thumbnailUrl: String,
    val durationSeconds: String,
    val spotifyUrl: String
) {
    override fun equals(other: Any?): Boolean = (other as? TrackDto)?.id?.equals(id) ?: false
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artistsName.hashCode()
        result = 31 * result + albumName.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        result = 31 * result + durationSeconds.hashCode()
        result = 31 * result + spotifyUrl.hashCode()
        return result
    }
}