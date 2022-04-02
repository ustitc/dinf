package dinf.adapters

import dinf.domain.DiceDelete
import dinf.domain.DiceMetrics

class MetricDiceDelete(private val diceMetrics: DiceMetrics) : DiceDelete by DiceDelete({ dice ->
    val metric = diceMetrics.forDice(dice)
    diceMetrics.remove(metric)
})
