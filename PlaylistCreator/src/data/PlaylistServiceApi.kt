package com.ubb.david.data

import com.ubb.david.domain.PlaylistResponseDto
import com.ubb.david.domain.TrackDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlaylistServiceApi {

    @GET("playlists/{playlistId}/tracks")
    suspend fun getPlaylistTracks(@Path("playlistId") playlistId: String): Response<List<TrackDto>>

    @POST("playlists")
    suspend fun createPlaylist(@Body tracks: List<TrackDto>): Response<PlaylistResponseDto>

    @PUT("playlists/{playlistId}/tracks")
    suspend fun addTracksToPlaylist(@Path("playlistId") playlistId: String, @Body tracks: List<TrackDto>): Response<Unit>

    @DELETE("playlists/{playlistId}/tracks/{trackId}")
    suspend fun removeTrackFromPlaylist(@Path("playlistId") playlistId: String, @Path("trackId") trackId: String): Response<Unit>
}