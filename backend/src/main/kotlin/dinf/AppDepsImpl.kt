package dinf

import dinf.adapters.BCryptPasswordFactory
import dinf.adapters.SqliteDiceFactory
import dinf.adapters.SqliteSearchIndexRepository
import dinf.adapters.SqliteDiceRepository
import dinf.adapters.FailoverSearchIndexRepository
import dinf.adapters.HashidsPublicIDFactory
import dinf.adapters.MeiliSearchIndexRepository
import dinf.adapters.SqliteUserPrincipalFactory
import dinf.adapters.SqliteUserPrincipalRepository
import dinf.auth.PasswordFactory
import dinf.auth.UserPrincipalService
import dinf.config.Configuration
import dinf.config.URL
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import org.hashids.Hashids

class AppDepsImpl(private val meiliDeps: MeiliDeps, private val cfg: Configuration) : AppDeps {

    private val diceMetricRepository = DiceMetricRepository.InMemory()
    private val passwordFactory: PasswordFactory = BCryptPasswordFactory()
    private val userPrincipalService: UserPrincipalService = UserPrincipalService(
        repo = SqliteUserPrincipalRepository(
            passwordFactory = passwordFactory
        ),
        factory = SqliteUserPrincipalFactory(),
        passwordFactory = passwordFactory,
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
            shareHashids = hashids(cfg.urls.share),
            editHashids = hashids(cfg.urls.edit)
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
                diceMetricRepository = diceMetricRepository()
            )
        )
    }

    override fun userPrincipalService(): UserPrincipalService {
        return userPrincipalService
    }

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
