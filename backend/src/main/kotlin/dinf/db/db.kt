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
        .load()
        .migrate()

    migrateEdges()
}

fun <R> transaction(block: Connection.() -> R): R =
    dataSource.connection.run {
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

private fun migrateEdges() {
    transaction {
        prepareStatement("SELECT id, edges FROM dices")
            .use { ps ->
                prepareStatement("DELETE FROM edges").use { it.execute() }
                ps.executeQuery().forEach { rs ->
                    val id = rs.getLong("id")
                    val edges = rs.getString("edges").split("\n")
                    edges.filter { it.isNotBlank() }.forEach { edge ->
                        prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?)")
                            .also {
                                it.setString(1, edge)
                                it.setLong(2, id)
                            }.use { it.execute() }
                    }
                }
            }

    }
}
