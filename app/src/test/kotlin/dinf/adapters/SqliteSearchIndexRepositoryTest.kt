package dinf.adapters

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class SqliteSearchIndexRepositoryTest : StringSpec({

    listeners(SqliteListener())

    "dice is found by name" {
        createDice("pinky")
        val repository = SqliteSearchIndexRepository()

        val result = repository.search("pinky")

        result shouldHaveSize 1
    }

    "dice is found by part of name" {
        createDice("pinky")
        val repository = SqliteSearchIndexRepository()

        val result = repository.search("pi")

        result shouldHaveSize 1
    }

})
