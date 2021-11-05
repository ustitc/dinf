package dinf.backend

import dinf.backend.plugins.configureHTTP
import dinf.backend.plugins.configureRouting
import dinf.backend.plugins.configureSecurity
import dinf.backend.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureHTTP()
        configureSecurity()
    }.start(wait = true)
}