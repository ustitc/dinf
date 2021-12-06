package dinf.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dinf.backend.plugins.configureHTTP
import dinf.backend.plugins.configureRouting
import dinf.backend.plugins.configureSecurity
import dinf.backend.plugins.configureSerialization
import dinf.exposed.DiceTable
import dinf.exposed.UserTable
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level

fun main() {
    configureDatabase()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureHTTP()
        configureSecurity()
        configureRouting()
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

private fun configureDatabase() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            UserTable, DiceTable
        )
    }
}