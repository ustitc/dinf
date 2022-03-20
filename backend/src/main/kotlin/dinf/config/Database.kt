package dinf.config

import com.zaxxer.hikari.HikariConfig

data class Database(val jdbcURL: String) {

    fun toHikariConfig(): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = jdbcURL
        config.isAutoCommit = false
        return config
    }

}
