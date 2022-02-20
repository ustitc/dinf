package dinf

import dinf.config.readConfiguration
import dinf.db.configureDatabase
import dinf.plugins.configureCallLogging
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import dinf.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.flow.launchIn

fun main() {
    val cfg = readConfiguration()

    configureDatabase(cfg.database)

    val meiliDeps = MeiliDeps(cfg.search)
    val appDeps = AppDepsImpl(meiliDeps)
    val reindexJob = ReindexJob(
        dices = appDeps.dices(),
        delay = cfg.search.reindex,
        meiliDiceIndex = meiliDeps.meiliDiceIndex()
    )

    embeddedServer(Netty, port = cfg.server.port, host = "0.0.0.0") {
        reindexJob.asFlow().launchIn(this)

        configureSerialization()
        configureRouting(cfg, appDeps)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}
