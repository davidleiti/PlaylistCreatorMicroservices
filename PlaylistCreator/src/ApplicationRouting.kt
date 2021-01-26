package com.ubb.david

import com.ubb.david.data.NetworkController
import com.ubb.david.data.Result
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.delete
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route

@KtorExperimentalLocationsAPI
fun Routing.playlistAppRouting(networkController: NetworkController) {
    authenticate("FirebaseAuthProvider") {
        route("/playlist") {
            post {
                when (val result = networkController.createNewPlaylist()) {
                    is Result.Success -> call.respond(HttpStatusCode.OK, result.data)
                    is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
                }
            }
            put("tracks") {
                call.parameters["albumId"]?.let { albumId ->
                    when (val result = networkController.addAlbumTracksToPlaylist(albumId)) {
                        is Result.Success -> call.respond(HttpStatusCode.OK, result.data)
                        is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
                    }
                } ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            delete<TrackLocation> { trackLocation ->
                val trackId = trackLocation.id
                when (val result = networkController.removeTrackFromPlaylist(trackId)) {
                    is Result.Success -> call.respond(HttpStatusCode.OK, true)
                    is Result.Error -> call.respond(HttpStatusCode.InternalServerError, result.errorMessage.orEmpty())
                }
            }
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("/tracks/{id}")
data class TrackLocation(val id: String)