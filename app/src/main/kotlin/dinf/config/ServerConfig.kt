package dinf.config

data class ServerConfig(
    val port: Int = 8080,
    val baseURL: String = "http://localhost:8080"
)
