package com.ubb.david.data

data class Track(
    val id: String,
    val title: String,
    val artistsName: String,
    val albumId: String,
    val albumName: String,
    val spotifyUrl: String,
    val durationSeconds: Long,
    val thumbnailUrl: String,
) {
    override fun equals(other: Any?): Boolean = (other as? Track)?.id?.equals(id) ?: false
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artistsName.hashCode()
        result = 31 * result + albumName.hashCode()
        result = 31 * result + spotifyUrl.hashCode()
        result = 31 * result + durationSeconds.hashCode()
        return result
    }
}