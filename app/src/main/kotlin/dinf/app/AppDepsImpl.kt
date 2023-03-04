package dinf.app

import dinf.app.adapters.BCryptPasswordFactory
import dinf.app.adapters.HashidsPublicIDFactory
import dinf.app.adapters.SqliteDiceRepository
import dinf.app.adapters.SqliteEdgeRepository
import dinf.app.adapters.SqliteUserFactory
import dinf.app.auth.EmailPasswordService
import dinf.app.auth.OAuthService
import dinf.app.auth.PasswordFactory
import dinf.app.config.AppConfig
import dinf.app.config.TogglesConfig
import dinf.app.config.HashConfig
import dinf.app.html.components.DiceCardComponentFactory
import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.plugins.isLoginedUser
import dinf.app.routes.DiceResource
import dinf.app.services.DiceViewService
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
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()
    private val dicePublicIdFactory: PublicIDFactory = HashidsPublicIDFactory(
        hashids = hashids(cfg.publicId.dice)
    )
    private val edgePublicIdFactory: PublicIDFactory = HashidsPublicIDFactory(
        hashids = hashids(cfg.publicId.edge)
    )
    private val userPublicIdFactory: PublicIDFactory = HashidsPublicIDFactory(
        hashids = hashids(cfg.publicId.edge)
    )
    private val diceCardComponentFactory: DiceCardComponentFactory = DiceCardComponentFactory(dicePublicIdFactory)

    override fun diceService(): DiceService {
        return DiceService(
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

    override fun diceViewService(): DiceViewService {
        return DiceViewService(
            diceService = diceService(),
            diceIdFactory = dicePublicIdFactory,
            edgeIdFactory = edgePublicIdFactory,
            userIdFactory = userPublicIdFactory
        )
    }

    override val toggles: TogglesConfig = cfg.toggles

    private fun hashids(url: HashConfig): Hashids {
        return Hashids(url.salt, url.length)
    }
}
