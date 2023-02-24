package dinf

import dinf.config.readConfiguration
import dinf.db.configureDatabase
import dinf.plugins.configureAuth
import dinf.plugins.configureCallLogging
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.resources.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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

        val meiliDeps = MeiliDeps(cfg.search)
        val appDeps = AppDepsImpl(meiliDeps, cfg, httpClient)

        launch {
            populateSearchIndex(appDeps)
        }

        install(Resources)
        configureAuth(appDeps, cfg, httpClient)
        configureRouting(cfg, appDeps)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}
