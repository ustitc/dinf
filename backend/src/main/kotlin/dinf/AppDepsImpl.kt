package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceFactory
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDices
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.HashIDsImpl
import dinf.adapters.MeiliDiceFactory
import dinf.adapters.MeiliDiceSearch
import dinf.config.Configuration
import dinf.config.URL
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceFactory
import dinf.domain.DiceSearch
import dinf.domain.Dices
import dinf.domain.HashIDs
import org.hashids.Hashids

class AppDepsImpl(private val meiliDeps: MeiliDeps, private val cfg: Configuration) : AppDeps {

    private val diceMetrics = DiceMetrics.InMemory()

    override fun dices(): Dices {
        return DBDices()
    }

    override fun diceMetrics(): DiceMetrics {
        return diceMetrics
    }

    override fun diceGet(): DiceGet {
        return DiceGet.TopByClicks(
            dices = dices(),
            metrics = diceMetrics
        )
    }

    override fun diceDelete(): DiceDelete {
        return DiceDelete.Logging(
            DiceDelete.Composite(
                DBDiceDelete(),
                DiceDelete { dice -> diceMetrics().removeForDice(dice) }
            )
        )
    }

    override fun diceSearch(): DiceSearch {
        return DiceSearch.PopularFirst(
            search = FailoverDiceSearch(
                main = MeiliDiceSearch(meiliDeps.meiliDiceIndex(), dices()),
                fallback = DBDiceSearch()
            ),
            metrics = diceMetrics()
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

    override fun shareHashIDs(): HashIDs {
        val hashids = hashids(cfg.urls.share)
        return HashIDsImpl(hashids)
    }

    override fun editHashIDs(): HashIDs {
        val hashids = hashids(cfg.urls.edit)
        return HashIDsImpl(hashids)
    }

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
