package dinf

import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceFactory
import dinf.domain.DiceSearch
import dinf.domain.Dices
import dinf.domain.HashIDs

interface AppDeps {

    fun dices(): Dices

    fun diceGet(): DiceGet

    fun diceDelete(): DiceDelete

    fun diceSearch(): DiceSearch

    fun diceFactory(): DiceFactory

    fun diceMetrics(): DiceMetrics

    fun shareHashIDs(): HashIDs

    fun editHashIDs(): HashIDs

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

        override fun diceFactory(): DiceFactory {
            return DiceFactory.Stub()
        }

        override fun diceMetrics(): DiceMetrics {
            return DiceMetrics.InMemory()
        }

        override fun shareHashIDs(): HashIDs {
            return HashIDs.Stub()
        }

        override fun editHashIDs(): HashIDs {
            return HashIDs.Stub()
        }
    }
}
