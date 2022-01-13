package dinf.backend

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.zaxxer.hikari.HikariDataSource
import dinf.backend.config.Configuration
import dinf.backend.plugins.configureHTTP
import dinf.backend.plugins.configureRouting
import dinf.backend.plugins.configureSerialization
import dinf.exposed.DiceTable
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
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
    val dices = DBDices()

    embeddedServer(Netty, port = config.server.port, host = "0.0.0.0") {
        configureSerialization()
        configureHTTP()
        configureRouting(config, dices)
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

private fun configureDatabase(database: dinf.backend.config.Database) {
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