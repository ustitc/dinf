package dinf.meilisearch

import dinf.AppDeps
import dinf.MeiliDeps
import dinf.adapters.MeiliDiceFactory
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory

suspend fun populateMeilisearch(appDeps: AppDeps, meiliDeps: MeiliDeps) {
    val logger = LoggerFactory.getLogger("dinf.meilisearch")
    val meili = MeiliDiceFactory(meiliDeps.meiliDiceIndex())
    val dices = appDeps.diceRepository().flow().toList()
    dices.forEach { dice ->
        meili.create(dice)
    }
    logger.info("Populated meilisearch with ${dices.count()} dices")
}
