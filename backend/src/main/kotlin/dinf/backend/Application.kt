package dinf.backend

import dinf.backend.plugins.configureHTTP
import dinf.backend.plugins.configureRouting
import dinf.backend.plugins.configureSecurity
import dinf.backend.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureHTTP()
        configureSecurity()
        install(CallLogging) {
            level = Level.INFO
            format {
                val status = it.response.status()
                val httpMethod = it.request.httpMethod.value
                val uri = it.request.uri
                val headers = it.request.headers.entries().joinToString { h -> "${h.key}: ${h.value}" }
                "$status: $httpMethod $uri headers: $headers"
            }
        }
    }.start(wait = true)
}