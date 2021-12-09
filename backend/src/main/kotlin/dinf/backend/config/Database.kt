package dinf.backend.config

import com.zaxxer.hikari.HikariConfig
import java.util.*

data class Database(
    val user: String,
    val password: String,
    val database: String,
    val port: Int,
    val server: String
) {

    fun toHikariConfig(): HikariConfig {
        val properties = Properties()
        properties["dataSourceClassName"] = "org.postgresql.ds.PGSimpleDataSource"
        properties["dataSource.user"] = user
        properties["dataSource.password"] = password
        properties["dataSource.databaseName"] = database
        properties["dataSource.portNumber"] = port
        properties["dataSource.serverName"] = server
        return HikariConfig(properties)
    }

}
