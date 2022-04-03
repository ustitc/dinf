package dinf.meilisearch

import dinf.AppDeps
import dinf.MeiliDeps
import dinf.adapters.MeiliDiceSave
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory

suspend fun populateMeilisearch(appDeps: AppDeps, meiliDeps: MeiliDeps) {
    val logger = LoggerFactory.getLogger("dinf.meilisearch")
    val meili = MeiliDiceSave(meiliDeps.meiliDiceIndex())
    val dices = appDeps.dices().flow().toList()
    dices.forEach { dice ->
        meili.invoke(dice)
    }
    logger.info("Populated meilisearch with ${dices.count()} dices")
}
