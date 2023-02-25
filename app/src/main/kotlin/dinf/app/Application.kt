package dinf.app

import dinf.app.config.readConfiguration
import dinf.app.db.configureDatabase
import dinf.app.plugins.configureAuth
import dinf.app.plugins.configureCallLogging
import dinf.app.plugins.configureMetrics
import dinf.app.plugins.configureRouting
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.resources.*
import kotlinx.serialization.json.Json

lateinit var deps: AppDeps

fun main() {
    val cfg = readConfiguration()

    embeddedServer(Netty, port = cfg.server.port, host = "0.0.0.0") {
        configureDatabase(cfg.database)

        val httpClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        deps = AppDepsImpl(cfg, httpClient)

        install(Resources)
        configureAuth(cfg, httpClient)
        configureRouting(cfg)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}
