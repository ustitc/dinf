package dinf.app

import dinf.app.adapters.BCryptPasswordFactory
import dinf.app.adapters.HashidsPublicIDFactory
import dinf.app.adapters.SqliteDiceFactory
import dinf.app.adapters.SqliteDiceRepository
import dinf.app.adapters.SqliteEdgeRepository
import dinf.app.adapters.SqliteUserFactory
import dinf.app.auth.EmailPasswordService
import dinf.app.auth.OAuthService
import dinf.app.auth.PasswordFactory
import dinf.app.config.AppConfig
import dinf.app.config.TogglesConfig
import dinf.app.config.URLConfig
import dinf.app.html.components.DiceCardComponentFactory
import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.plugins.isLoginedUser
import dinf.app.routes.DiceResource
import dinf.app.services.DicePageService
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.app.services.PublicIDFactory
import io.ktor.client.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.application.*
import org.hashids.Hashids

class AppDepsImpl(
    cfg: AppConfig,
    private val httpClient: HttpClient
) : AppDeps {

    private val diceRepository: DiceRepository = SqliteDiceRepository()
    private val diceFactory: DiceFactory = SqliteDiceFactory()
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()
    private val publicIDFactory: PublicIDFactory = HashidsPublicIDFactory(
        hashids = hashids(cfg.urls.share)
    )
    private val diceCardComponentFactory: DiceCardComponentFactory = DiceCardComponentFactory(publicIDFactory)

    override fun diceService(): DiceService {
        return DiceService(
            diceFactory = diceFactory,
            diceRepository = diceRepository,
            edgeRepository = SqliteEdgeRepository()
        )
    }

    override fun emailPasswordService(): EmailPasswordService {
        return EmailPasswordService(
            userFactory = SqliteUserFactory(),
            nameSource = { "Happy User" },
            passwordFactory = passwordFactory
        )
    }

    override fun oAuthService(): OAuthService {
        return OAuthService(
            httpClient = httpClient,
            nameSource = { "Happy User" },
            userFactory = SqliteUserFactory(),
        )
    }

    override fun diceFeedComponentFactory(call: ApplicationCall): DiceFeedComponentFactory {
        val newDiceURL = href(ResourcesFormat(), DiceResource.New())
        return DiceFeedComponentFactory(
            newDiceURL = newDiceURL,
            diceCardComponentFactory = diceCardComponentFactory,
            showAddButton = toggles.showUserButtons && call.isLoginedUser()
        )
    }

    override fun dicePageService(): DicePageService {
        return DicePageService(
            diceService = diceService(),
            publicIDFactory = publicIDFactory
        )
    }

    override val toggles: TogglesConfig = cfg.toggles

    private fun hashids(url: URLConfig): Hashids {
        return Hashids(url.salt, url.length)
    }
}
