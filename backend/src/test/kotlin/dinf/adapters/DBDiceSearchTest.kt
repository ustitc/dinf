package dinf.adapters

import dinf.domain.Count
import dinf.domain.Page
import dinf.domain.SearchQuery
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class DBDiceSearchTest : StringSpec({

    listeners(DBListener())

    "dice is found by name" {
        createDiceEntity("pinky")
        val searchDice = DBDiceSearch()

        val result = searchDice.invoke(SearchQuery("pinky"))

        result shouldHaveSize 1
    }

    "dice is found by part of name" {
        createDiceEntity("pinky")
        val searchDice = DBDiceSearch()

        val result = searchDice.invoke(SearchQuery("pi"))

        result shouldHaveSize 1
    }

    "limit by count" {
        createDiceEntity("pinky")
        createDiceEntity("pixar")
        val searchDice = DBDiceSearch()

        val result = searchDice.invoke(SearchQuery("pi", Page(1), Count(1)))

        result shouldHaveSize 1
    }

    "show nothing if page is too large" {
        createDiceEntity("pinky")
        val searchDice = DBDiceSearch()

        val result = searchDice.invoke(SearchQuery("pi", Page(10), Count(10)))

        result shouldHaveSize 0
    }

})
