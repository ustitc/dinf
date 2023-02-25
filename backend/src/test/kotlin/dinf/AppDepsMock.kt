package dinf

import dinf.auth.EmailPasswordService
import dinf.auth.OAuthService
import dinf.config.TogglesConfig
import dinf.domain.DiceService
import dinf.html.components.DiceFeedComponentFactory
import io.ktor.server.application.*
import io.mockk.mockk

class AppDepsMock : AppDeps {

    override fun diceService(): DiceService = mockk(relaxed = true)

    override fun emailPasswordService(): EmailPasswordService = mockk()

    override fun oAuthService(): OAuthService = mockk()

    override fun diceFeedComponentFactory(call: ApplicationCall): DiceFeedComponentFactory = mockk()

    override val toggles: TogglesConfig = TogglesConfig()

}