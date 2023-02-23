package dinf

import dinf.auth.UserPrincipalService
import dinf.config.Toggles
import dinf.domain.DiceFactory
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import io.mockk.mockk

class AppDepsMock : AppDeps {

    override fun diceRepository(): DiceRepository = mockk()

    override fun diceFactory(): DiceFactory = mockk()

    override fun diceMetricRepository(): DiceMetricRepository = mockk()

    override fun publicIDFactory(): PublicIDFactory = mockk()

    override fun searchIndexRepository(): SearchIndexRepository = mockk()

    override fun diceService(): DiceService = mockk(relaxed = true)

    override fun userPrincipalService(): UserPrincipalService = mockk()

    override val toggles: Toggles = Toggles()

}