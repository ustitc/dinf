package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder

class DiceSearchTest : StringSpec({

    "dices with higher click count must be first" {
        val metrics = DiceMetrics.Simple()
        val first = Dice.Stub("first")
        val second = Dice.Stub("second")
        val third = Dice.Stub("third")

        metrics.increment(first)
        metrics.increment(first)
        metrics.increment(second)

        val search = DiceSearch.PopularFirst(
            search = DiceSearch.Stub {
                listOf(third, second, first)
            },
            metrics = metrics
        )

        search.forText("any") shouldContainInOrder listOf(first, second, third)
    }

})
