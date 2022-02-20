package dinf

import dinf.adapters.DBDices
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.Dices

object Dependencies {

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

}


