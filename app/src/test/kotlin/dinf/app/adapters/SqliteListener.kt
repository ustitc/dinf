package dinf.app.adapters

import dinf.app.config.DatabaseConfig
import dinf.app.db.configureDatabase
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase

class SqliteListener : TestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        configureDatabase(DatabaseConfig(jdbcURL = "jdbc:sqlite::memory:"))
    }
}
