package com.ubb.david.data

import com.google.gson.Gson
import com.ubb.david.domain.TrackDto
import com.ubb.david.domain.TrackListResponseDto
import io.ktor.http.HttpStatusCode
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64

class SpotifyController : MusicSourceController {

    private val retrofit: Retrofit = createRetrofit()
    private val spotifyApi: SpotifyApi = retrofit.create(SpotifyApi::class.java)

    private var accessToken: String? = null

    override suspend fun getTopTracks(): ApiResult<List<TrackDto>> {
        return try {
            performSecureCall { secureHeaders -> spotifyApi.getTopTracks(secureHeaders, TOP_TRACKS_PLAYLIST_ID) }
        } catch (ex: Throwable) {
            println("[getTopTracks] Exception occurred: $ex")
            ApiResult.Error(HttpStatusCode.InternalServerError, "Internal exception occurred: ${ex.message}")
        }
    }

    override suspend fun getAlbumTracks(albumId: String): ApiResult<List<TrackDto>> {
        return try {
            performSecureCall { secureHeaders ->
                spotifyApi.getAlbumTracks(secureHeaders, albumId)

            }
        } catch (ex: Throwable) {
            println("[getTopTracks] Exception occurred: $ex")
            ApiResult.Error(HttpStatusCode.InternalServerError, "Internal exception occurred: ${ex.message}")
        }
    }

    private suspend fun <T : TrackListResponseDto> performSecureCall(call: suspend (secureHeaders: Map<String, String>) -> Response<T>): ApiResult<List<TrackDto>> {
        return accessToken?.let { token ->
            val headers = mapOf("Authorization" to "Bearer $token")
            val response = call(headers)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                ApiResult.Success(responseBody.toTrackDtos())
            } else {
                println("Failed to retrieve top tracks, cause: ${response.errorBody()}")
                ApiResult.Error(HttpStatusCode(response.code(), response.message()), response.message())
            }
        } ?: run {
            val basicAuthToken: String = Base64.getEncoder().encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray())
            val headers = mapOf("Authorization" to "Basic $basicAuthToken")
            val response = spotifyApi.getAccessToken(headers, AUTH_URL, GRANT_TYPE)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                accessToken = body.accessToken
                performSecureCall(call)
            } else {
                println("Failed to retrieve access token, cause: ${response.errorBody()}")
                ApiResult.Error(HttpStatusCode.Unauthorized, response.message())
            }
        }
    }

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_SPOTIFY_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().create()))
        .build()

    private fun createOkHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(httpLoggingInterceptor())
        .build()

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
}