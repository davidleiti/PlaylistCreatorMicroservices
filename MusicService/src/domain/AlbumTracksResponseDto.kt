package domain

import com.google.gson.annotations.SerializedName
import com.ubb.david.data.toTrackDto
import com.ubb.david.domain.TrackDto
import com.ubb.david.domain.TrackListResponseDto

data class AlbumTracksResponseDto(
    @SerializedName("href")
    val href: String = "",
    @SerializedName("items")
    val items: List<AlbumItem> = listOf(),
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("next")
    val next: Any? = null,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("previous")
    val previous: Any? = null,
    @SerializedName("total")
    val total: Int = 0
): TrackListResponseDto {
    override fun toTrackDtos(): List<TrackDto> = items.map { albumItem -> albumItem.toTrackDto() }
}

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

data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("expires_in")
    val expiresIn: Int = 0,
    @SerializedName("scope")
    val scope: String = "",
    @SerializedName("token_type")
    val tokenType: String = ""
)

data class ExternalUrls(
    @SerializedName("spotify")
    val spotify: String = ""
)

data class AlbumItem(
    @SerializedName("artists")
    val artists: List<Artist> = listOf(),
    @SerializedName("available_markets")
    val availableMarkets: List<String> = listOf(),
    @SerializedName("disc_number")
    val discNumber: Int = 0,
    @SerializedName("duration_ms")
    val durationMs: Int = 0,
    @SerializedName("explicit")
    val explicit: Boolean = false,
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
    @SerializedName("preview_url")
    val previewUrl: String = "",
    @SerializedName("track_number")
    val trackNumber: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uri")
    val uri: String = ""
)

