package dinf.app.db

import com.zaxxer.hikari.HikariDataSource
import dinf.app.config.DatabaseConfig
import org.flywaydb.core.Flyway
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource

private lateinit var dataSource: DataSource

fun configureDatabase(databaseConfig: DatabaseConfig) {
    dataSource = HikariDataSource(databaseConfig.toHikariConfig())
    migrateDatabase()
}

private fun migrateDatabase() {
    Flyway.configure()
        .dataSource(dataSource)
        .baselineOnMigrate(true)
        .mixed(true)
        .load()
        .migrate()
}

fun connection(): Connection {
    return dataSource.connection
}

fun <R> transaction(block: Connection.() -> R): R =
    connection().run {
        return try {
            val result = block.invoke(this)
            commit()
            result
        } catch (e: SQLException) {
            rollback()
            throw e
        } finally {
            close()
        }
    }

fun <R> sql(sql: String, block: PreparedStatement.() -> R): R {
    return transaction {
        prepareStatement(sql).use(block)
    }
}
