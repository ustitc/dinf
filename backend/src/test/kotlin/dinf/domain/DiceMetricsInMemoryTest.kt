package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList

class DiceMetricsInMemoryTest : StringSpec({

    val metrics = DiceMetrics.InMemory()

    suspend fun Dice.addClicks(clicks: Clicks) {
        repeat(clicks.toInt()) {
            metrics.forDice(this).addClick()
        }
    }

    beforeTest {
        metrics.clear()
    }

    "metric for new dice contains zero clicks" {
        val dice = Dice.Stub()

        val metric = metrics.forDice(dice)

        metric.clicks shouldBe 0L
    }

    "increment will add one click" {
        val dice = Dice.Stub()

        metrics
            .forDice(dice)
            .addClick()

        metrics.forDice(dice).clicks shouldBe 1L
    }

    "no duplicates" {
        val dice = Dice.Stub()
        metrics.forDice(dice)
        metrics.forDice(dice)

        val all = metrics.popularSNs().toList()

        all shouldHaveSize 1
    }

    "serials with highest metric first" {
        val first = Dice.Stub()
        val second = Dice.Stub()
        val third = Dice.Stub()

        first.addClicks(2)
        second.addClicks(10)
        third.addClicks(4)

        metrics.popularSNs().toList().shouldContainInOrder(
            second.serialNumber,
            third.serialNumber,
            first.serialNumber
        )
    }

    "metric deletes" {
        val dice = Dice.Stub()
        val metric = metrics.forDice(dice)

        metrics.remove(metric)

        metrics.popularSNs().toList() shouldHaveSize 0
    }

    "removing unknown metric doesn't cause and error" {
        metrics.remove(Metric.Simple(10))

        metrics.popularSNs().toList() shouldHaveSize 0
    }

})
