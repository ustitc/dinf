package dinf

import dinf.auth.UserPrincipalService
import dinf.config.Toggles
import dinf.domain.DiceFactory
import dinf.domain.DiceMetricRepository
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

}
