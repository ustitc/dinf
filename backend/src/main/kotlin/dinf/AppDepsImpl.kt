package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceFactory
import dinf.adapters.DBSearchIndexRepository
import dinf.adapters.DBDiceRepository
import dinf.adapters.FailoverSearchIndexRepository
import dinf.adapters.HashIDFactoryImpl
import dinf.adapters.MeiliSearchIndexRepository
import dinf.config.Configuration
import dinf.config.URL
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.HashIDFactory
import dinf.domain.SearchIndexRepository
import org.hashids.Hashids

class AppDepsImpl(private val meiliDeps: MeiliDeps, private val cfg: Configuration) : AppDeps {

    private val diceMetricRepository = DiceMetricRepository.InMemory()

    override fun diceRepository(): DiceRepository {
        return DBDiceRepository()
    }

    override fun diceMetricRepository(): DiceMetricRepository {
        return diceMetricRepository
    }

    override fun diceGet(): DiceGet {
        return DiceGet.TopByClicks(
            diceRepository = diceRepository(),
            metrics = diceMetricRepository
        )
    }

    override fun diceDelete(): DiceDelete {
        return DiceDelete.Logging(
            DiceDelete.Composite(
                DBDiceDelete(),
                DiceDelete { dice -> diceMetricRepository().removeForDice(dice) }
            )
        )
    }

    override fun diceFactory(): DiceFactory {
        return DiceFactory.Logging(
            DBDiceFactory()
        )
    }

    override fun shareHashIDFactory(): HashIDFactory {
        val hashids = hashids(cfg.urls.share)
        return HashIDFactoryImpl(hashids)
    }

    override fun editHashIDFactory(): HashIDFactory {
        val hashids = hashids(cfg.urls.edit)
        return HashIDFactoryImpl(hashids)
    }

    override fun searchIndexRepository(): SearchIndexRepository {
        return FailoverSearchIndexRepository(
            main = MeiliSearchIndexRepository(meiliDeps.meiliDiceIndex()),
            fallback = DBSearchIndexRepository()
        )
    }

    override fun diceService(): DiceService {
        return DiceService.Impl(
            diceFactory = diceFactory(),
            diceRepository = diceRepository(),
            searchIndexRepository = searchIndexRepository(),
            hashIDFactory = editHashIDFactory(),
            diceMetricRepository = diceMetricRepository()
        )
    }

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
