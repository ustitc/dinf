package dinf.adapters

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class DBSearchIndexRepositoryTest : StringSpec({

    listeners(DBListener())

    "dice is found by name" {
        createDiceEntity("pinky")
        val repository = DBSearchIndexRepository()

        val result = repository.search("pinky")

        result shouldHaveSize 1
    }

    "dice is found by part of name" {
        createDiceEntity("pinky")
        val repository = DBSearchIndexRepository()

        val result = repository.search("pi")

        result shouldHaveSize 1
    }

})
