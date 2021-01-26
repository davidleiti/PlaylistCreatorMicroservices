package com.ubb.david

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.ubb.david.data.PlaylistResponseDto
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
    post("/playlists") {
        val tracks = call.receiveOrNull<String>()?.let { gson.fromJson<List<Track>>(it) } ?: listOf()
        val playlistUuid = repository.createPlaylist(tracks)
        call.respond(HttpStatusCode.OK, PlaylistResponseDto(playlistUuid))
    }
    get<PlaylistTracksPath> { path ->
        repository.getTracks(path.playlistId)?.let { tracks ->
            call.respond(HttpStatusCode.OK, tracks)
        } ?: run {
            call.respond(HttpStatusCode.NotFound)
        }
    }
    put<PlaylistTracksPath> { path ->
        val tracks = call.receiveOrNull<String>()?.let { gson.fromJson<List<Track>>(it) } ?: listOf()
        repository.addTracks(path.playlistId, tracks.toList())
        call.respond(HttpStatusCode.OK)
    }
    delete<PlaylistTrackPath> { path ->
        val responseStatus =
            if (repository.removeTrack(path.playlistId, path.trackId)) HttpStatusCode.OK else HttpStatusCode.NotFound
        call.respond(responseStatus)
    }
}

internal inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

@KtorExperimentalLocationsAPI
@Location("/playlists/{playlistId}/tracks")
data class PlaylistTracksPath(val playlistId: String)

@KtorExperimentalLocationsAPI
@Location("/playlists/{playlistId}/tracks/{trackId}")
data class PlaylistTrackPath(val playlistId: String, val trackId: String)