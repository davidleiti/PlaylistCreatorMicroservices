package com.ubb.david.playlistcreator.data

import com.ubb.david.playlistcreator.domain.TrackDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistApi {
    @POST("/playlist")
    suspend fun createNewPlaylist(): Response<List<TrackDto>>

    @PUT("/playlist/tracks")
    suspend fun addTracksFromAlbum(@Query("albumId") albumId: String): Response<List<TrackDto>>

    @DELETE("/playlist/tracks/{id}")
    suspend fun removeTrack(@Path("id") trackId: String): Response<Boolean>
}