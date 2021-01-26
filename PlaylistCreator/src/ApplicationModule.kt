package com.ubb.david

import com.ubb.david.auth.firebase
import com.ubb.david.config.FirebaseAdmin
import com.ubb.david.data.NetworkController
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.gson.gson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.routing.routing
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.module() {
    FirebaseAdmin.init()
    install(Locations)
    install(DataConversion)
    install(Authentication) { firebase() }
    install(ContentNegotiation) { gson { setPrettyPrinting() } }
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    routing { playlistAppRouting(NetworkController()) }
}

