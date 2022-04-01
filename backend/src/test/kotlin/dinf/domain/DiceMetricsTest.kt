package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DiceMetricsTest : StringSpec({

    val metrics = DiceMetrics.InMemory()

    "metric for new dice contains zero clicks" {
        val dice = Dice.Stub()

        val metric = metrics.forDice(dice)

        metric.clicks() shouldBe 0L
    }

    "increment will add one click" {
        val dice = Dice.Stub()

        metrics
            .forDice(dice)
            .addClick()

        metrics.forDice(dice).clicks() shouldBe 1L
    }

})
