package dinf.domain

import dinf.types.PInt
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

typealias Count = PInt
typealias Page = PInt

fun interface DiceGet : suspend (Page, Count) -> List<Dice> {

    class Empty : DiceGet by DiceGet({ _, _ -> emptyList() })

    class TopByClicks(private val dices: Dices, private val metrics: DiceMetrics) : DiceGet by DiceGet({ page, count ->
        val toDrop = (page - 1) * count.toInt()
        dices.flow()
            .map { it to metrics.forDiceOrZero(it) }
            .toList()
            .sortedByDescending { it.second.clicks }
            .map { it.first }
            .drop(toDrop)
            .take(count.toInt())
    })

}
