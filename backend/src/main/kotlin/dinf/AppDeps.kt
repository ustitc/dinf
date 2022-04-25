package dinf

import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository

interface AppDeps {

    fun diceRepository(): DiceRepository

    fun diceFactory(): DiceFactory

    fun diceMetricRepository(): DiceMetricRepository

    fun publicIDFactory(): PublicIDFactory

    fun searchIndexRepository(): SearchIndexRepository

    fun diceService(): DiceService

    class Stub : AppDeps {

        override fun diceRepository(): DiceRepository {
            return DiceRepository.Stub()
        }

        override fun diceFactory(): DiceFactory {
            return DiceFactory.Stub()
        }

        override fun diceMetricRepository(): DiceMetricRepository {
            return DiceMetricRepository.InMemory()
        }

        override fun publicIDFactory(): PublicIDFactory {
            return PublicIDFactory.Stub()
        }

        override fun searchIndexRepository(): SearchIndexRepository {
            return SearchIndexRepository.Stub()
        }

        override fun diceService(): DiceService {
            return DiceService.Stub()
        }
    }
}
