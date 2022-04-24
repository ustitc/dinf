package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceFactory
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDiceRepository
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.HashIDFactoryImpl
import dinf.adapters.MeiliDiceFactory
import dinf.adapters.MeiliDiceSearch
import dinf.config.Configuration
import dinf.config.URL
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceSearch
import dinf.domain.DiceRepository
import dinf.domain.HashIDFactory
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

    override fun diceSearch(): DiceSearch {
        return DiceSearch.PopularFirst(
            search = FailoverDiceSearch(
                main = MeiliDiceSearch(meiliDeps.meiliDiceIndex(), diceRepository()),
                fallback = DBDiceSearch()
            ),
            metrics = diceMetricRepository()
        )
    }

    override fun diceFactory(): DiceFactory {
        return DiceFactory.Logging(
            DiceFactory.Composite(
                DBDiceFactory(),
                MeiliDiceFactory(meiliDeps.meiliDiceIndex())
            )
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

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
