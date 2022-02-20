package dinf

import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices

interface AppDeps {

    fun dices(): Dices

    fun diceGet(): DiceGet

    fun diceDelete(): DiceDelete

    fun diceSearch(): DiceSearch

    fun diceSave(): DiceSave

    fun diceMetrics(): DiceMetrics

    class Stub : AppDeps {

        override fun dices(): Dices {
            return Dices.Stub()
        }

        override fun diceGet(): DiceGet {
            return DiceGet.Empty()
        }

        override fun diceDelete(): DiceDelete {
            return DiceDelete.Stub()
        }

        override fun diceSearch(): DiceSearch {
            return DiceSearch.Stub()
        }

        override fun diceSave(): DiceSave {
            return DiceSave.Stub()
        }

        override fun diceMetrics(): DiceMetrics {
            return DiceMetrics.InMemory()
        }
    }
}
