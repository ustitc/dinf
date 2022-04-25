package dinf

import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.HashIDFactory
import dinf.domain.SearchIndexRepository

interface AppDeps {

    fun diceRepository(): DiceRepository

    fun diceGet(): DiceGet

    fun diceFactory(): DiceFactory

    fun diceMetricRepository(): DiceMetricRepository

    fun shareHashIDFactory(): HashIDFactory

    fun editHashIDFactory(): HashIDFactory

    fun searchIndexRepository(): SearchIndexRepository

    fun diceService(): DiceService

    class Stub : AppDeps {

        override fun diceRepository(): DiceRepository {
            return DiceRepository.Stub()
        }

        override fun diceGet(): DiceGet {
            return DiceGet.Empty()
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

        override fun searchIndexRepository(): SearchIndexRepository {
            return SearchIndexRepository.Stub()
        }

        override fun diceService(): DiceService {
            return DiceService.Stub()
        }
    }
}
