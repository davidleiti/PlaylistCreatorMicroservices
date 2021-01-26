package com.ubb.david

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object PlaylistApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, module = Application::module).start(wait = true)
    }
}