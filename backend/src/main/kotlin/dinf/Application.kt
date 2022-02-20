package dinf

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.zaxxer.hikari.HikariDataSource
import dinf.config.Configuration
import dinf.exposed.DiceTable
import dinf.plugins.configureCallLogging
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import dinf.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.flow.launchIn
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun main() {
    val config = ConfigLoader.Builder()
        .also {
            val path = System.getenv("CONFIG_PATH")
            if (path != null) {
                it.addSource(PropertySource.file(file = File(path), optional = true))
            }
        }
        .addSource(PropertySource.resource("/application.toml"))
        .build()
        .loadConfigOrThrow<Configuration>()

    configureDatabase(config.database)

    val meiliDeps = MeiliDependencies(config.search)
    val appDeps = AppDependencies(meiliDeps)
    val reindexJob = ReindexJob(
        dices = appDeps.dices(),
        delay = config.search.reindex,
        meiliDiceIndex = meiliDeps.meiliDiceIndex()
    )

    embeddedServer(Netty, port = config.server.port, host = "0.0.0.0") {
        reindexJob.asFlow().launchIn(this)

        configureSerialization()
        configureRouting(config, appDeps)
        configureCallLogging()
        configureMetrics()
    }.start(wait = true)
}

private fun configureDatabase(database: dinf.config.Database) {
    val config = database.toHikariConfig()
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            DiceTable
        )
    }
}