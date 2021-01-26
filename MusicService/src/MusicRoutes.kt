package com.ubb.david

import com.ubb.david.data.ApiResult
import com.ubb.david.data.SpotifyController
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

@KtorExperimentalLocationsAPI
fun Routing.music(controller: SpotifyController) {
    get("tracks/top-50") {
        when (val result = controller.getTopTracks()) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> call.respond(result.httpStatusCode)
        }
    }
    get<Album> { album ->
        when (val result = controller.getAlbumTracks(album.albumId)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> call.respond(result.httpStatusCode)
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("albums/{albumId}")
data class Album(val albumId: String)