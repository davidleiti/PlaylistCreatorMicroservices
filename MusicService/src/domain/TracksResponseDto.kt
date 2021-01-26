package com.ubb.david.domain

import com.google.gson.annotations.SerializedName
import com.ubb.david.data.toTrackDto

data class TracksResponseDto(
    @SerializedName("href")
    val href: String = "",
    @SerializedName("items")
    val items: List<Item> = listOf(),
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("total")
    val total: Int = 0
): TrackListResponseDto {
    override fun toTrackDtos(): List<TrackDto> = items.map { item -> item.track.toTrackDto() }
}

data class AddedBy(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls = ExternalUrls(),
    @SerializedName("href")
    val href: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uri")
    val uri: String = ""
)

data class Album(
    @SerializedName("album_type")
    val albumType: String = "",
    @SerializedName("artists")
    val artists: List<Artist> = listOf(),
    @SerializedName("available_markets")
    val availableMarkets: List<String> = listOf(),
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls = ExternalUrls(),
    @SerializedName("href")
    val href: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("images")
    val images: List<Image> = listOf(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String = "",
    @SerializedName("total_tracks")
    val totalTracks: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uri")
    val uri: String = ""
)

data class Artist(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls = ExternalUrls(),
    @SerializedName("href")
    val href: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uri")
    val uri: String = ""
)

data class ExternalIds(
    @SerializedName("isrc")
    val isrc: String = ""
)

data class ExternalUrls(
    @SerializedName("spotify")
    val spotify: String = ""
)

data class Image(
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("url")
    val url: String = "",
    @SerializedName("width")
    val width: Int = 0
)

data class Item(
    @SerializedName("added_at")
    val addedAt: String = "",
    @SerializedName("added_by")
    val addedBy: AddedBy = AddedBy(),
    @SerializedName("is_local")
    val isLocal: Boolean = false,
    @SerializedName("primary_color")
    val primaryColor: Any? = null,
    @SerializedName("track")
    val track: Track = Track(),
    @SerializedName("video_thumbnail")
    val videoThumbnail: VideoThumbnail = VideoThumbnail()
)

data class Track(
    @SerializedName("album")
    val album: Album = Album(),
    @SerializedName("artists")
    val artists: List<Artist> = listOf(),
    @SerializedName("available_markets")
    val availableMarkets: List<String> = listOf(),
    @SerializedName("disc_number")
    val discNumber: Int = 0,
    @SerializedName("duration_ms")
    val durationMs: Int = 0,
    @SerializedName("episode")
    val episode: Boolean = false,
    @SerializedName("explicit")
    val explicit: Boolean = false,
    @SerializedName("external_ids")
    val externalIds: ExternalIds = ExternalIds(),
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls = ExternalUrls(),
    @SerializedName("href")
    val href: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_local")
    val isLocal: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("popularity")
    val popularity: Int = 0,
    @SerializedName("preview_url")
    val previewUrl: Any? = null,
    @SerializedName("track")
    val track: Boolean = false,
    @SerializedName("track_number")
    val trackNumber: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uri")
    val uri: String = ""
)

data class VideoThumbnail(
    @SerializedName("url")
    val url: Any? = null
)