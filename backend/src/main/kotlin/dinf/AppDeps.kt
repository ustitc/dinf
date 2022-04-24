package dinf

import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceSearch
import dinf.domain.DiceRepository
import dinf.domain.HashIDFactory

interface AppDeps {

    fun diceRepository(): DiceRepository

    fun diceGet(): DiceGet

    fun diceDelete(): DiceDelete

    fun diceSearch(): DiceSearch

    fun diceFactory(): DiceFactory

    fun diceMetricRepository(): DiceMetricRepository

    fun shareHashIDFactory(): HashIDFactory

    fun editHashIDFactory(): HashIDFactory

    class Stub : AppDeps {

        override fun diceRepository(): DiceRepository {
            return DiceRepository.Stub()
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

        override fun diceMetricRepository(): DiceMetricRepository {
            return DiceMetricRepository.InMemory()
        }

        override fun shareHashIDFactory(): HashIDFactory {
            return HashIDFactory.Stub()
        }

        override fun editHashIDFactory(): HashIDFactory {
            return HashIDFactory.Stub()
        }
    }
}
