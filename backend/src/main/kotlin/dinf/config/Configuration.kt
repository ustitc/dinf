package dinf.config

import dinf.htmx.HTMXConfiguration
import java.time.Duration

data class Configuration(
    val database: Database = Database("jdbc:sqlite::memory:"),
    val urls: URLs = URLs(),
    val server: Server = Server(),
    val search: Search = Search(),
    val htmx: HTMXConfiguration = HTMXConfiguration(timeout = Duration.ofSeconds(5))
)
