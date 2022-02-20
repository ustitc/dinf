package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceSave
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDices
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.MeiliDiceSearch
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices

class AppDependencies(private val meiliDependencies: MeiliDependencies) {

    private val diceMetrics = DiceMetrics.InMemory()

    fun dices(): Dices {
        return DBDices()
    }

    fun diceMetrics(): DiceMetrics {
        return diceMetrics
    }

    fun diceGet(): DiceGet {
        return DiceGet.TopByClicks(
            dices = dices(),
            metrics = diceMetrics
        )
    }

    fun diceDelete(): DiceDelete {
        return DBDiceDelete()
    }

    fun diceSearch(): DiceSearch {
        return DiceSearch.PopularFirst(
            search = FailoverDiceSearch(
                main = MeiliDiceSearch(meiliDependencies.meiliDiceIndex(), dices()),
                fallback = DBDiceSearch()
            ),
            metrics = diceMetrics()
        )
    }

    fun diceSave(): DiceSave {
        return DBDiceSave()
    }

}
