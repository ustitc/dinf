package dinf.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList

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

    class Simple(private val dices: Dices = Dices.Stub()) : DiceSearch {

        override suspend fun forText(text: String): List<Dice> {
            return dices.flow().filter { it.name.nbString.contains(text) }.toList()
        }
    }

}
