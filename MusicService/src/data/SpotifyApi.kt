package com.ubb.david.data

import com.ubb.david.domain.TracksResponseDto
import domain.AlbumTracksResponseDto
import domain.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface SpotifyApi {
    @POST
    @FormUrlEncoded
    suspend fun getAccessToken(
        @HeaderMap headers: Map<String, String>,
        @Url authUrl: String,
        @Field("grant_type") grantType: String
    ): Response<AuthResponseDto>

    @GET(TOP_TRACKS_URL)
    suspend fun getTopTracks(@HeaderMap headers: Map<String, String>, @Path("id") id: String): Response<TracksResponseDto>

    @GET(ALBUM_TRACKS_URL)
    suspend fun getAlbumTracks(@HeaderMap headers: Map<String, String>, @Path("id") id: String): Response<AlbumTracksResponseDto>
}