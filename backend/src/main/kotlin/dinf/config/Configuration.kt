package dinf.config

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import dev.ustits.htmx.HTMXConfiguration
import java.io.File
import java.time.Duration

data class Configuration(
    val database: Database = Database("jdbc:sqlite::memory:"),
    val urls: URLs = URLs(),
    val server: Server = Server(),
    val search: Search = Search(),
    val htmx: HTMXConfiguration = HTMXConfiguration(timeout = Duration.ofSeconds(5))
)

fun readConfiguration(): Configuration {
    return configLoader().loadConfigOrThrow()
}

private fun configLoader(): ConfigLoader {
    return ConfigLoader.Builder()
        .also {
            val path = System.getenv("CONFIG_PATH")
            if (path != null) {
                it.addSource(PropertySource.file(file = File(path), optional = true))
            }
        }
        .addSource(PropertySource.resource("/application.toml"))
        .build()
}
