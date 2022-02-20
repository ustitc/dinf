package dinf.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
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
