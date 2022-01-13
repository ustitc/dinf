package dinf.test

import io.kotest.extensions.testcontainers.perTest
import org.testcontainers.containers.PostgreSQLContainer

private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:13").apply {
    withDatabaseName("dinf")
    withUsername("dinf")
    withPassword("dinf123")
}

private val hikariListener = PostgresTestListener(postgresContainer)

val postgresTestListeners = listOf(postgresContainer.perTest(), hikariListener)

val sqLiteTestListener = listOf(SQLiteTestListener())