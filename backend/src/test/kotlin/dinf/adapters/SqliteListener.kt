package dinf.adapters

import dinf.config.Database
import dinf.db.configureDatabase
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase

class SqliteListener : TestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        configureDatabase(Database(jdbcURL = "jdbc:sqlite::memory:"))
    }
}
