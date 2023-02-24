package dinf

import dinf.adapters.BCryptPasswordFactory
import dinf.adapters.FailoverSearchIndexRepository
import dinf.adapters.HashidsPublicIDFactory
import dinf.adapters.MeiliSearchIndexRepository
import dinf.adapters.SqliteDiceFactory
import dinf.adapters.SqliteDiceOwner
import dinf.adapters.SqliteDiceRepository
import dinf.adapters.SqliteSearchIndexRepository
import dinf.adapters.SqliteUserFactory
import dinf.auth.AuthenticationService
import dinf.auth.EmailPasswordsService
import dinf.auth.OAuthService
import dinf.auth.PasswordFactory
import dinf.config.Configuration
import dinf.config.TogglesConfig
import dinf.config.URL
import dinf.domain.DiceFactory
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import io.ktor.client.*
import org.hashids.Hashids

class AppDepsImpl(
    private val meiliDeps: MeiliDeps,
    private val cfg: Configuration,
    httpClient: HttpClient
) : AppDeps {

    private val diceMetricRepository = DiceMetricRepository.InMemory()
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()
    private val authenticationService: AuthenticationService = AuthenticationService(
        emailPasswordsService = EmailPasswordsService(
            passwordFactory = passwordFactory
        ),
        oAuthService = OAuthService(httpClient),
        userFactory = SqliteUserFactory(),
        nameSource = { "Happy User" }
    )

    override fun diceRepository(): DiceRepository {
        return SqliteDiceRepository()
    }

    override fun diceMetricRepository(): DiceMetricRepository {
        return diceMetricRepository
    }

    override fun diceFactory(): DiceFactory {
        return SqliteDiceFactory()
    }

    override fun publicIDFactory(): PublicIDFactory {
        return HashidsPublicIDFactory(
            hashids = hashids(cfg.urls.share)
        )
    }

    override fun searchIndexRepository(): SearchIndexRepository {
        return FailoverSearchIndexRepository(
            main = MeiliSearchIndexRepository(meiliDeps.meiliDiceIndex()),
            fallback = SqliteSearchIndexRepository()
        )
    }

    override fun diceService(): DiceService {
        return DiceService.Logging(
            DiceService.Impl(
                diceFactory = diceFactory(),
                diceRepository = diceRepository(),
                searchIndexRepository = searchIndexRepository(),
                publicIDFactory = publicIDFactory(),
                diceMetricRepository = diceMetricRepository(),
                diceOwnerFactory = SqliteDiceOwner.Companion
            )
        )
    }

    override fun authenticationService(): AuthenticationService {
        return authenticationService
    }

    override val toggles: TogglesConfig = cfg.toggles

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
