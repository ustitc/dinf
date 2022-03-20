package dinf.adapters

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class DBDiceSearchTest : StringSpec({

    listeners(DBListener())

    "dice is found by name" {
        createDiceEntity("pinky")
        val searchDice = DBDiceSearch()

        val result = searchDice.forText("pinky")

        result shouldHaveSize 1
    }

    "dice is found by part of name" {
        createDiceEntity("pinky")
        val searchDice = DBDiceSearch()

        val result = searchDice.forText("pi")

        result shouldHaveSize 1
    }

})
