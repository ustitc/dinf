package dinf.adapters

import dinf.domain.DiceMetrics
import dinf.domain.DiceSave

class MetricDiceSave(private val metrics: DiceMetrics) : DiceSave by DiceSave({
    metrics.forDice(it)
    it
})
