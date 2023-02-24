package dinf

import dinf.auth.AuthenticationService
import dinf.config.TogglesConfig
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

    fun authenticationService(): AuthenticationService

    val toggles: TogglesConfig

}
