package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList

class DiceMetricsInMemoryTest : StringSpec({

    val metrics = DiceMetrics.InMemory()

    beforeTest {
        metrics.clear()
    }

    "no metric for unknown dice" {
        val dice = Dice.Stub()

        val metric = metrics.forDice(dice)

        metric shouldBe null
    }

    "return metric for existing dice" {
        val dice = Dice.Stub()
        metrics.create(dice, Metric.zero())

        val metric = metrics.forDice(dice)

        metric shouldNotBe null
    }

    "return zero metric for unknown dice" {
        val dice = Dice.Stub()

        val metric = metrics.forDiceOrZero(dice)

        metric.clicks shouldBe 0L
    }

    "no duplicates" {
        val dice = Dice.Stub()
        metrics.create(dice, Metric.zero())
        metrics.create(dice, Metric.zero())

        val all = metrics.popularIDs().toList()

        all shouldHaveSize 1
    }

    "serials with highest metric first" {
        val first = Dice.Stub()
        val second = Dice.Stub()
        val third = Dice.Stub()

        metrics.create(first, Metric.Simple(2))
        metrics.create(second, Metric.Simple(10))
        metrics.create(third, Metric.Simple(4))

        metrics.popularIDs().toList().shouldContainInOrder(
            second.id,
            third.id,
            first.id
        )
    }

    "metric deletes" {
        val dice = Dice.Stub()
        metrics.create(dice, Metric.zero())

        metrics.removeForDice(dice)

        metrics.popularIDs().toList() shouldHaveSize 0
    }

    "removing unknown metric doesn't cause and error" {
        metrics.removeForDice(Dice.Stub())

        metrics.popularIDs().toList() shouldHaveSize 0
    }

})
