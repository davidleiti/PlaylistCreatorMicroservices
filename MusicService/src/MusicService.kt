package com.ubb.david

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object MusicService {
    @KtorExperimentalLocationsAPI
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
    }
}