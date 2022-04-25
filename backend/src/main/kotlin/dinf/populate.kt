package dinf

import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory

suspend fun populateSearchIndex(appDeps: AppDeps) {
    val logger = LoggerFactory.getLogger("dinf")
    val indexRepo = appDeps.searchIndexRepository()
    val dices = appDeps.diceRepository().flow().toList()
    dices.forEach { dice ->
        indexRepo.add(dice)
    }
    logger.info("Populated search indexes with ${dices.count()} dices")
}
