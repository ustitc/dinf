package dinf.backend

import dinf.domain.Dice
import dinf.domain.DiceSearch
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize

class FailoverDiceSearchTest : StringSpec({

    "use main method result if no error happened" {
        val expected = List(3) { Dice.Stub() }

        val search = FailoverDiceSearch(
            main = DiceSearch.Stub { expected },
            fallback = DiceSearch.Empty()
        )

        val result = search.forText("test")

        result shouldContainExactly expected
    }

    "switch to fallback on error" {
        val search = FailoverDiceSearch(
            main = DiceSearch.Stub { error("") },
            fallback = DiceSearch.Empty()
        )

        val result = search.forText("test")

        result shouldHaveSize 0
    }

})
