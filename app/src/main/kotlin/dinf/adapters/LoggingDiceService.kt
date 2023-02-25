package dinf.adapters

import dinf.domain.Count
import dinf.domain.Dice
import dinf.domain.DiceService
import dinf.domain.Edges
import dinf.domain.ID
import dinf.domain.Name
import dinf.domain.Page
import dinf.domain.PublicID
import dinf.domain.SearchQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggingDiceService(private val service: DiceService) : DiceService {

    private val logger: Logger = LoggerFactory.getLogger(DiceService::class.java)

    override suspend fun saveDice(name: Name, edges: Edges, userID: ID): PublicID {
        val hashID = service.saveDice(name, edges, userID)
        logger.info("Saved dice for id: ${hashID.toID()}")
        return hashID
    }

    override suspend fun findDiceByPublicID(publicID: String): Dice? {
        val dice = service.findDiceByPublicID(publicID)
        if (dice == null) {
            logger.warn("Found no dice for shareID: $publicID")
        } else {
            logger.info("Found dice with id ${dice.id} for shareID: $publicID")
        }
        return dice
    }

    override suspend fun findDiceByPublicIdAndUserId(publicID: String, userID: ID): Dice? {
        val dice = service.findDiceByPublicIdAndUserId(publicID, userID)
        if (dice == null) {
            logger.info("Not found dice for publicID=${publicID}, userId=${userID}")
        } else {
            logger.info("Found diceID=${dice.id} for publicId=${publicID}, userId=${userID}")
        }
        return dice
    }

    override suspend fun find(page: Page, count: Count): List<Dice> {
        val dices = service.find(page, count)
        logger.debug("Found ${dices.size} for page: $page and count: $count")
        return dices
    }

    override suspend fun search(query: SearchQuery): List<Dice> {
        val dices = service.search(query)
        logger.info("Found ${dices.size} dices for query: $query")
        return dices
    }

    override suspend fun deleteByPublicIdAndUserId(publicID: String, userId: ID) {
        service.deleteByPublicIdAndUserId(publicID, userId)
        logger.info("Deleted dice for id=$publicID, userId=${userId}")
    }

}