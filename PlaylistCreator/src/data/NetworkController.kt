package com.ubb.david.data

import com.google.gson.Gson
import com.ubb.david.domain.TrackDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkController {

    companion object {
        private const val BASE_PLAYLIST_SERVICE_URL = "http://localhost:3001"
        private const val BASE_MUSIC_SERVICE_URL = "http://localhost:3002"
    }

    private val musicServiceApi: MusicServiceApi = createMusicServiceApi()
    private val playlistServiceApi: PlaylistServiceApi = createPlaylistServiceApi()

    suspend fun createNewPlaylist(): Result<List<TrackDto>> {
        return when (val topTracksResult = performApiCall { musicServiceApi.getTopTracks() }) {
            is Result.Success -> {
                when (val createPlaylistResult = performApiCall { playlistServiceApi.addTracksToPlaylist(topTracksResult.data) }) {
                    is Result.Success -> Result.Success(topTracksResult.data)
                    is Result.Error -> {
                        println("Error retrieving saving top tracks, cause: ${createPlaylistResult.exception} - ${createPlaylistResult.errorMessage}")
                        createPlaylistResult
                    }
                }
            }
            is Result.Error -> {
                println("Error retrieving top tracks, cause: ${topTracksResult.exception} - ${topTracksResult.errorMessage}")
                topTracksResult
            }
        }
    }

    suspend fun addAlbumTracksToPlaylist(albumId: String): Result<List<TrackDto>> {
        return when (val albumTracksResult = performApiCall { musicServiceApi.getAlbumTracks(albumId) }) {
            is Result.Success -> {
                when (val addTracksToPlaylistResult = performApiCall { playlistServiceApi.addTracksToPlaylist(albumTracksResult.data) }) {
                    is Result.Success -> Result.Success(albumTracksResult.data)
                    is Result.Error -> addTracksToPlaylistResult
                }
            }
            is Result.Error -> albumTracksResult
        }
    }

    suspend fun removeTrackFromPlaylist(trackId: String): Result<Unit> = performApiCall { playlistServiceApi.removeTrackFromPlaylist(trackId) }

    private suspend fun <T : Any> performApiCall(call: suspend () -> Response<T>): Result<T> = withContext(Dispatchers.IO) {
        try {
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(errorMessage = "${response.code()} - ${response.errorBody()}")
            }
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    private fun createPlaylistServiceApi(): PlaylistServiceApi = Retrofit.Builder()
            .baseUrl(BASE_PLAYLIST_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().create()))
            .build()
            .create(PlaylistServiceApi::class.java)

    private fun createMusicServiceApi(): MusicServiceApi = Retrofit.Builder()
            .baseUrl(BASE_MUSIC_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().create()))
            .build()
            .create(MusicServiceApi::class.java)

}