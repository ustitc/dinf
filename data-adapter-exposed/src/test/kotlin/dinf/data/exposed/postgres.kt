package dinf.data.exposed

import io.kotest.extensions.testcontainers.perTest
import org.testcontainers.containers.PostgreSQLContainer

private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:13").apply {
    withDatabaseName("dinf")
    withUsername("dinf")
    withPassword("dinf123")
}

private val hikariListener = HikariTestListener(postgresContainer)

val postgresTestListeners = listOf(postgresContainer.perTest(), hikariListener)
