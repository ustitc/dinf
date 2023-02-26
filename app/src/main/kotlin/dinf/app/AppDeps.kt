package dinf.app

import dinf.app.auth.EmailPasswordService
import dinf.app.auth.OAuthService
import dinf.app.config.TogglesConfig
import dinf.domain.DiceService
import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.services.DicePageService
import io.ktor.server.application.*

interface AppDeps {

    fun diceService(): DiceService

    fun emailPasswordService(): EmailPasswordService

    fun oAuthService(): OAuthService

    fun diceFeedComponentFactory(call: ApplicationCall): DiceFeedComponentFactory

    fun dicePageService(): DicePageService

    val toggles: TogglesConfig

}
