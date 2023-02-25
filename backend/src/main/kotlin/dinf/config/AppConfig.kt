package dinf.config

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import dev.ustits.htmx.HTMXConfiguration
import java.io.File
import java.time.Duration

data class AppConfig(
    val database: DatabaseConfig = DatabaseConfig("jdbc:sqlite::memory:"),
    val urls: URLsConfig = URLsConfig(),
    val server: ServerConfig = ServerConfig(),
    val htmx: HTMXConfiguration = HTMXConfiguration(timeout = Duration.ofSeconds(5)),
    val toggles: TogglesConfig = TogglesConfig(),
    val login: LoginConfig = LoginConfig()
)

fun readConfiguration(): AppConfig {
    return configLoader().loadConfigOrThrow()
}

private fun configLoader(): ConfigLoader {
    return ConfigLoaderBuilder.default()
        .also {
            val path = System.getenv("CONFIG_PATH")
            if (path != null) {
                it.addPropertySource(PropertySource.file(file = File(path), optional = true))
            }
        }
        .addPropertySource(PropertySource.resource("/application.toml"))
        .build()
}
