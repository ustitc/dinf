package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceSave
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDices
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.MeiliDiceSave
import dinf.adapters.MeiliDiceSearch
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices

class AppDepsImpl(private val meiliDeps: MeiliDeps) : AppDeps {

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

    override fun diceSave(): DiceSave {
        return DiceSave.Logging(
            DiceSave.Composite(
                DBDiceSave(),
                MeiliDiceSave(meiliDeps.meiliDiceIndex())
            )
        )
    }

}
