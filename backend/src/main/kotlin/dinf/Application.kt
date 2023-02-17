package dinf

import dinf.config.readConfiguration
import dinf.db.configureDatabase
import dinf.plugins.configureAuth
import dinf.plugins.configureCallLogging
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.resources.*
import kotlinx.coroutines.launch

fun main() {
    val cfg = readConfiguration()

    embeddedServer(Netty, port = cfg.server.port, host = "0.0.0.0") {
        configureDatabase(cfg.database)

        val meiliDeps = MeiliDeps(cfg.search)
        val appDeps = AppDepsImpl(meiliDeps, cfg)

        launch {
            populateSearchIndex(appDeps)
        }

        install(Resources)
        configureAuth(appDeps)
        configureRouting(cfg, appDeps)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}
