package com.ubb.david

import com.ubb.david.data.NetworkController
import com.ubb.david.data.Result
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.put
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post

@KtorExperimentalLocationsAPI
fun Routing.playlistAppRouting(networkController: NetworkController) {
    authenticate("FirebaseAuthProvider") {
        post("/playlists") {
            when (val result = networkController.createNewPlaylist()) {
                is Result.Success -> call.respond(HttpStatusCode.OK, result.data)
                is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
            }
        }
        get<PlaylistTracksPath> { path ->
            when (val result = networkController.getPlaylistTracks(path.playlistId)) {
                is Result.Success -> call.respond(HttpStatusCode.OK, result.data)
                is Result.Error -> call.respond(HttpStatusCode.NotFound)
            }
        }
        put<PlaylistTracksPath> { path ->
            call.parameters["albumId"]?.let { albumId ->
                val albumName = call.parameters["albumName"]
                val thumbnailUrl = call.parameters["thumbnailUrl"]
                when (val result = networkController.addAlbumTracksToPlaylist(path.playlistId, albumId, albumName.orEmpty(), thumbnailUrl.orEmpty())) {
                    is Result.Success -> call.respond(HttpStatusCode.OK, result.data)
                    is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
                }
            } ?: run {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        delete<PlaylistTrackPath> { path ->
            when (val result = networkController.removeTrackFromPlaylist(path.playlistId, path.trackId)) {
                is Result.Success -> call.respond(HttpStatusCode.OK, true)
                is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
            }
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("/playlists/{playlistId}/tracks")
data class PlaylistTracksPath(val playlistId: String)

@KtorExperimentalLocationsAPI
@Location("/playlists/{playlistId}/tracks/{trackId}")
data class PlaylistTrackPath(val playlistId: String, val trackId: String)