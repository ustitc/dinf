package dinf.db

import com.zaxxer.hikari.HikariDataSource
import dinf.config.Database
import org.flywaydb.core.Flyway
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

private lateinit var dataSource: DataSource

fun configureDatabase(database: Database) {
    dataSource = HikariDataSource(database.toHikariConfig())
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
