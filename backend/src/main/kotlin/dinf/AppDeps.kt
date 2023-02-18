package dinf

import dinf.auth.PasswordFactory
import dinf.auth.UserPrincipalFactory
import dinf.auth.UserPrincipalRepository
import dinf.auth.UserPrincipalService
import dinf.config.Toggles
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

    fun userPrincipalService(): UserPrincipalService

    val toggles: Toggles

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

        override fun userPrincipalService(): UserPrincipalService {
            return UserPrincipalService(
                repo = UserPrincipalRepository.InMemory(),
                factory = UserPrincipalFactory.Stub(),
                passwordFactory = PasswordFactory.Stub(),
                nameSource = { "Happy User" }
            )
        }

        override val toggles: Toggles = Toggles()
    }
}
