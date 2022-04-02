package dinf

import dinf.config.readConfiguration
import dinf.db.configureDatabase
import dinf.plugins.configureCallLogging
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import dinf.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val cfg = readConfiguration()

    configureDatabase(cfg.database)

    val meiliDeps = MeiliDeps(cfg.search)
    val appDeps = AppDepsImpl(meiliDeps)

    embeddedServer(Netty, port = cfg.server.port, host = "0.0.0.0") {
        configureSerialization()
        configureRouting(cfg, appDeps)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}
