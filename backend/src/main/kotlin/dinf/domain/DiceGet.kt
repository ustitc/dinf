package dinf.domain

import dinf.types.PInt
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

typealias Count = PInt
typealias Page = PInt

fun interface DiceGet : suspend (Page, Count) -> List<Dice> {

    class Empty : DiceGet {
        override suspend fun invoke(p1: Page, p2: Count): List<Dice> {
            return emptyList()
        }
    }

    class TopByClicks(private val dices: Dices, private val metrics: DiceMetrics) : DiceGet {

        override suspend fun invoke(p1: Page, p2: Count): List<Dice> {
            val toDrop = (p1 - 1) * p2.toInt()
            return dices
                .flow()
                .map { it to metrics.forDice(it) }
                .toList()
                .sortedByDescending { runBlocking { it.second.clicks() } }
                .map { it.first }
                .drop(toDrop)
                .take(p2.toInt())
        }
    }

}
