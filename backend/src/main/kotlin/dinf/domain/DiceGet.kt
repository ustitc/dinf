package dinf.domain

import dinf.types.PInt
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

typealias Count = PInt

fun interface DiceGet : suspend (Count) -> List<Dice> {

    class TopByClicks(private val dices: Dices, private val metrics: DiceMetrics) : DiceGet {

        override suspend fun invoke(p1: Count): List<Dice> {
            return dices
                .flow()
                .toList()
                .map { it to metrics.forDice(it) }
                .sortedByDescending { runBlocking { it.second.clicks() } }
                .map { it.first }
                .take(p1.toInt())
        }
    }

}
