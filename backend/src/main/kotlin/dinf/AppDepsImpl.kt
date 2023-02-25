package dinf

import dinf.adapters.BCryptPasswordFactory
import dinf.adapters.HashidsPublicIDFactory
import dinf.adapters.SqliteDiceFactory
import dinf.adapters.SqliteDiceOwner
import dinf.adapters.SqliteDiceRepository
import dinf.adapters.SqliteSearchIndexRepository
import dinf.adapters.SqliteUserFactory
import dinf.auth.EmailPasswordService
import dinf.auth.OAuthService
import dinf.auth.PasswordFactory
import dinf.config.AppConfig
import dinf.config.TogglesConfig
import dinf.config.URLConfig
import dinf.domain.DiceFactory
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import dinf.html.components.DiceCardComponentFactory
import dinf.html.components.DiceFeedComponentFactory
import dinf.plugins.isLoginedUser
import dinf.routes.DiceResource
import io.ktor.client.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.application.*
import org.hashids.Hashids

class AppDepsImpl(
    private val cfg: AppConfig,
    private val httpClient: HttpClient
) : AppDeps {

    private val diceRepository: DiceRepository = SqliteDiceRepository()
    private val diceFactory: DiceFactory = SqliteDiceFactory()
    private val diceMetricRepository: DiceMetricRepository = DiceMetricRepository.InMemory()
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()
    private val publicIDFactory: PublicIDFactory = HashidsPublicIDFactory(
        hashids = hashids(cfg.urls.share)
    )
    private val diceCardComponentFactory: DiceCardComponentFactory = DiceCardComponentFactory(publicIDFactory)
    private val searchIndexRepository: SearchIndexRepository = SqliteSearchIndexRepository()

    override fun diceService(): DiceService {
        return DiceService.Logging(
            DiceService.Impl(
                diceFactory = diceFactory,
                diceRepository = diceRepository,
                searchIndexRepository = searchIndexRepository,
                publicIDFactory = publicIDFactory,
                diceMetricRepository = diceMetricRepository,
                diceOwnerFactory = SqliteDiceOwner.Companion
            )
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

    override val toggles: TogglesConfig = cfg.toggles

    private fun hashids(url: URLConfig): Hashids {
        return Hashids(url.salt, url.length)
    }
}
