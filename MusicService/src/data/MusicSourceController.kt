package com.ubb.david.data

import com.ubb.david.domain.TrackDto
import io.ktor.http.HttpStatusCode

interface MusicSourceController {
    suspend fun getTopTracks(): ApiResult<List<TrackDto>>
    suspend fun getAlbumTracks(albumId: String): ApiResult<List<TrackDto>>
}

sealed class ApiResult<T> {
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val httpStatusCode: HttpStatusCode, val message: String) : ApiResult<T>()
}