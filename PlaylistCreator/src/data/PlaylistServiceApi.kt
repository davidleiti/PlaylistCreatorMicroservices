package com.ubb.david.data

import com.ubb.david.domain.TrackDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlaylistServiceApi {

    @GET("playlist/tracks")
    suspend fun getPlaylistTracks(): Response<List<TrackDto>>

    @POST("playlist/tracks")
    suspend fun addTracksToPlaylist(@Body tracks: List<TrackDto>): Response<Unit>

    @DELETE("playlist/tracks/{trackId}")
    suspend fun removeTrackFromPlaylist(@Path("trackId") trackId: String): Response<Unit>

}