package dinf.backend.config

data class Configuration(
    val database: Database = Database("jdbc:sqlite::memory:"),
    val urls: URLs = URLs(),
    val server: Server = Server(),
    val search: Search = Search()
)
