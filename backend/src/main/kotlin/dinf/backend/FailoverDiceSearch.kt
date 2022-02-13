package dinf.backend

import dinf.domain.Dice
import dinf.domain.DiceSearch
import org.slf4j.LoggerFactory

class FailoverDiceSearch(
    private val main: DiceSearch,
    private val fallback: DiceSearch
) : DiceSearch {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun forText(text: String): List<Dice> {
        return try {
            main.forText(text)
        } catch (e: Exception) {
            log.error("Failed search. Switching to fallback method", e)
            fallback.forText(text)
        }
    }
}
