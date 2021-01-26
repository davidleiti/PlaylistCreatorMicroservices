package com.ubb.david.playlistcreator.data

import com.ubb.david.playlistcreator.domain.PlaylistDto
import com.ubb.david.playlistcreator.domain.TrackDto
import retrofit2.Response
import retrofit2.http.*

interface PlaylistApi {
    @POST("/playlists")
    suspend fun createNewPlaylist(): Response<PlaylistDto>

    @GET("/playlists/{playlistId}/tracks")
    suspend fun getPlaylistTracks(@Path("playlistId") playlistId: String): Response<List<TrackDto>>

    @PUT("/playlists/{playlistId}/tracks")
    suspend fun addTracksFromAlbum(
        @Path("playlistId") playlistId: String,
        @Query("albumId") albumId: String,
        @Query("albumName") albumName: String,
        @Query("thumbnailUrl") thumbnailUrl: String
    ): Response<List<TrackDto>>

    @DELETE("/playlists/{playlistId}/tracks/{trackId}")
    suspend fun removeTrack(@Path("playlistId") playlistId: String, @Path("trackId") trackId: String): Response<Boolean>
}