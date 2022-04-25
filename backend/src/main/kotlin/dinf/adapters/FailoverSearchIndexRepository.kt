package dinf.adapters

import dinf.domain.Dice
import dinf.domain.ID
import dinf.domain.SearchIndexRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FailoverSearchIndexRepository(
    private val main: SearchIndexRepository,
    private val fallback: SearchIndexRepository,
    private val logger: Logger = LoggerFactory.getLogger(FailoverSearchIndexRepository::class.java)
) : SearchIndexRepository {

    override fun add(dice: Dice) {
        try {
            main.add(dice)
        } catch (e: Exception) {
            logger.error("Failed to add dice to index. Switching to fallback method", e)
            fallback.add(dice)
        }
    }

    override fun search(text: String): List<ID> {
        return try {
            main.search(text)
        } catch (e: Exception) {
            logger.error("Failed search. Switching to fallback method", e)
            fallback.search(text)
        }
    }

}
