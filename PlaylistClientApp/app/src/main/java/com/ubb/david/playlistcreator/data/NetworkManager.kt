package com.ubb.david.playlistcreator.data

import com.google.gson.GsonBuilder
import com.ubb.david.playlistcreator.domain.TrackDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class NetworkManager {

    private val retrofit: Retrofit = createRetrofit()
    private val playlistApi: PlaylistApi = retrofit.create(PlaylistApi::class.java)

    suspend fun createPlaylist(): Result<List<TrackDto>> = performApiCall { playlistApi.createNewPlaylist() }

    suspend fun addAlbumTracks(albumId: String): Result<List<TrackDto>> = performApiCall { playlistApi.addTracksFromAlbum(albumId) }

    suspend fun removeTrack(trackId: String): Result<Boolean> = performApiCall { playlistApi.removeTrack(trackId) }

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

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create()))
            .build()

    private fun createOkHttpClient(): OkHttpClient {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.addInterceptor(AuthInterceptor())
            builder.addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY })
            builder.build()
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)
        }
    }

}

