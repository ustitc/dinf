package dinf.app

import dinf.app.auth.EmailPasswordService
import dinf.app.auth.OAuthService
import dinf.app.config.TogglesConfig
import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.services.DiceViewService
import dinf.domain.DiceService
import io.ktor.server.application.*
import io.mockk.mockk

class AppDepsMock : AppDeps {

    override fun diceService(): DiceService = mockk(relaxed = true)

    override fun emailPasswordService(): EmailPasswordService = mockk(relaxed = true)

    override fun oAuthService(): OAuthService = mockk(relaxed = true)

    override fun diceFeedComponentFactory(call: ApplicationCall): DiceFeedComponentFactory = mockk(relaxed = true)

    override fun diceViewService(): DiceViewService = mockk(relaxed = true)

    override val toggles: TogglesConfig = TogglesConfig()

}