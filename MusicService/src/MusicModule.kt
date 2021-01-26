package com.ubb.david

import com.ubb.david.data.SpotifyController
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.gson.gson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.routing.routing
import org.slf4j.event.Level

@KtorExperimentalLocationsAPI
fun Application.module() {
    install(ConditionalHeaders)
    install(CORS)
    install(Locations)
    install(DataConversion)
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation) { gson { setPrettyPrinting() } }

    routing { music(SpotifyController()) }
}
