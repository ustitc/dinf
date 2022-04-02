package dinf

import dinf.adapters.DBDiceDelete
import dinf.adapters.DBDiceSave
import dinf.adapters.DBDiceSearch
import dinf.adapters.DBDices
import dinf.adapters.FailoverDiceSearch
import dinf.adapters.MeiliDiceSave
import dinf.adapters.MeiliDiceSearch
import dinf.adapters.MetricDiceDelete
import dinf.adapters.MetricDiceSave
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class AppDepsImpl(private val meiliDeps: MeiliDeps) : AppDeps {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val diceMetrics = DiceMetrics.InMemory()

    init {
        populateMeiliAndMetrics()
    }

    private fun populateMeiliAndMetrics() {
        val meili = MeiliDiceSave(meiliDeps.meiliDiceIndex())
        val metric = MetricDiceSave(diceMetrics())
        runBlocking {
            val dices = dices().flow().toList()
            dices.forEach { dice ->
                meili.invoke(dice)
                metric.invoke(dice)
            }
            logger.info("Populated meilisearch and metrics with ${dices.count()} dices")
        }
    }

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
                MetricDiceDelete(diceMetrics())
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
                MeiliDiceSave(meiliDeps.meiliDiceIndex()),
                MetricDiceSave(diceMetrics())
            )
        )
    }

}
