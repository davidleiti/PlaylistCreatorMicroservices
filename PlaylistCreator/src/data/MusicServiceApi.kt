package com.ubb.david.data

import com.ubb.david.domain.TrackDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicServiceApi {

    @GET("tracks/top-50")
    suspend fun getTopTracks(): Response<List<TrackDto>>

    @GET("albums/{albumId}")
    suspend fun getAlbumTracks(@Path("albumId") albumId: String): Response<List<TrackDto>>

}