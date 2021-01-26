package com.ubb.david

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.ubb.david.data.Track
import com.ubb.david.domain.TrackRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.playlist(repository: TrackRepository) {
    val gson = GsonBuilder().create()
    route("/playlist") {
        get("/tracks") {
            call.respond(HttpStatusCode.OK, repository.localTracks)
        }
        post("/tracks") {
            val tracks = call.receiveOrNull<String>()?.let { gson.fromJson<List<Track>>(it) } ?: listOf()
            repository.addTracks(tracks.toList())
            call.respond(HttpStatusCode.OK)
        }
        delete<TrackPath> { track ->
            val responseStatus = if (repository.removeTrack(track.id)) HttpStatusCode.OK else HttpStatusCode.NotFound
            call.respond(responseStatus)
        }
    }
}

internal inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

@KtorExperimentalLocationsAPI
@Location("/tracks/{id}")
data class TrackPath(val id: String)