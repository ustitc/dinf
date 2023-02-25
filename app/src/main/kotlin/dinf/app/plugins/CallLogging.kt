package dinf.app.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureCallLogging() {
    install(CallLogging) {
        level = Level.DEBUG
        format {
            val status = it.response.status()
            val httpMethod = it.request.httpMethod.value
            val uri = it.request.uri
            val headers = it.request.headers.entries().joinToString { h -> "${h.key}: ${h.value}" }
            "$status: $httpMethod $uri headers: $headers"
        }
    }
}
