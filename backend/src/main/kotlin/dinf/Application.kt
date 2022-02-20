package dinf

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import com.zaxxer.hikari.HikariDataSource
import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceSave
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDices
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.MeiliDiceCollection
import dinf.adapters.MeiliDiceSave
import dinf.adapters.MeiliDiceSearch
import dinf.config.Configuration
import dinf.domain.DiceMetrics
import dinf.domain.DiceSearch
import dinf.plugins.configureMetrics
import dinf.plugins.configureRouting
import dinf.plugins.configureSerialization
import dinf.exposed.DiceTable
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.time.delay
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
    val meiliClient = Client(
        Config(config.search.url, config.search.key)
    )
    val dicesIndex = meiliClient.index(MeiliDiceCollection.indexName)
    val meiliDiceSave = MeiliDiceSave(dicesIndex)
    val dbDiceSave = DBDiceSave()
    val diceMetrics = DiceMetrics.Simple()
    val diceSearch = DiceSearch.PopularFirst(
        search = FailoverDiceSearch(
            main = MeiliDiceSearch(dicesIndex, dices),
            fallback = DBDiceSearch()
        ),
        metrics = diceMetrics
    )
    val diceDelete = DBDiceDelete()

    val scheduledEventFlow = flow {
        while (true) {
            delay(config.search.reindex)
            dices
                .flow()
                .onEach { emit(it) }
                .collect()
        }
    }

    embeddedServer(Netty, port = config.server.port, host = "0.0.0.0") {
        scheduledEventFlow.onEach {
            meiliDiceSave.create(it)
        }.launchIn(this)

        configureSerialization()
        configureRouting(config, dices, dbDiceSave, diceMetrics, diceSearch, diceDelete)
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