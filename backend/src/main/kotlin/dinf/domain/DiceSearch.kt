package dinf.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.time.delay
import java.time.Duration

interface DiceSearch {

    suspend fun forText(text: String): List<Dice>

    class Stub(private val block: () -> List<Dice>) : DiceSearch {

        override suspend fun forText(text: String): List<Dice> {
            return block()
        }
    }

    class Empty : DiceSearch {
        override suspend fun forText(text: String): List<Dice> {
            return emptyList()
        }
    }

    class Delay(private val main: DiceSearch, private val duration: Duration) : DiceSearch {
        override suspend fun forText(text: String): List<Dice> {
            delay(duration)
            return main.forText(text)
        }
    }

    class Simple(private val dices: Dices = Dices.Stub()) : DiceSearch {

        override suspend fun forText(text: String): List<Dice> {
            return dices.flow().filter { it.name.nbString.contains(text) }.toList()
        }
    }

    class PopularFirst(private val search: DiceSearch, private val metrics: DiceMetrics) : DiceSearch {

        override suspend fun forText(text: String): List<Dice> {
            return search
                .forText(text)
                .map { it to metrics.clicks(it) }
                .sortedByDescending { it.second }
                .map { it.first }
        }
    }

}
