package dinf.test

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dinf.exposed.DiceTable
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class SQLiteTestListener : TestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite::memory:"
        val ds = HikariDataSource(config)
        Database.connect(ds)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                DiceTable
            )
        }
    }
}
