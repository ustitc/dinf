package dinf.adapters

import dinf.config.DatabaseConfig
import dinf.db.configureDatabase
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase

class SqliteListener : TestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        configureDatabase(DatabaseConfig(jdbcURL = "jdbc:sqlite::memory:"))
    }
}
