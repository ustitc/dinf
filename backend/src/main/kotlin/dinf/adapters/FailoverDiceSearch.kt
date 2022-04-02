package dinf.adapters

import dinf.domain.DiceSearch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FailoverDiceSearch(
    private val main: DiceSearch,
    private val fallback: DiceSearch,
    private val logger: Logger = LoggerFactory.getLogger(FailoverDiceSearch::class.java)
) : DiceSearch by DiceSearch({ text ->
    try {
        main.invoke(text)
    } catch (e: Exception) {
        logger.error("Failed search. Switching to fallback method", e)
        fallback.invoke(text)
    }
})
