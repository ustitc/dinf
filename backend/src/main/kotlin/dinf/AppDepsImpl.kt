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
import dinf.config.Configuration
import dinf.config.TogglesConfig
import dinf.config.URLConfig
import dinf.domain.DiceFactory
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import io.ktor.client.*
import org.hashids.Hashids

class AppDepsImpl(
    private val cfg: Configuration,
    private val httpClient: HttpClient
) : AppDeps {

    private val diceMetricRepository = DiceMetricRepository.InMemory()
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()

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
        return SqliteSearchIndexRepository()
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

    override val toggles: TogglesConfig = cfg.toggles

    private fun hashids(url: URLConfig): Hashids {
        return Hashids(url.salt, url.length)
    }
}
