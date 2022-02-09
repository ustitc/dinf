package dinf.backend.config

import java.time.Duration

data class Search(
    val url: String = "http://localhost:7700",
    val key: String = "",
    val reindex: Duration = Duration.ofSeconds(60)
)
