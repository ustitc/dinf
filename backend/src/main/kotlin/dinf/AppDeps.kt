package dinf

import dinf.auth.EmailPasswordService
import dinf.auth.OAuthService
import dinf.config.TogglesConfig
import dinf.domain.DiceService
import dinf.html.components.DiceFeedComponentFactory
import io.ktor.server.application.*

interface AppDeps {

    fun diceService(): DiceService

    fun emailPasswordService(): EmailPasswordService

    fun oAuthService(): OAuthService

    fun diceFeedComponentFactory(call: ApplicationCall): DiceFeedComponentFactory

    val toggles: TogglesConfig

}
