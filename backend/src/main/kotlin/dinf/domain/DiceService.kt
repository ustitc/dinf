package dinf.domain

import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceService {

    suspend fun saveDice(name: Name, edges: Edges, userID: ID): PublicID
    suspend fun findDiceByPublicID(publicID: String): Dice?
    suspend fun findDiceByPublicIdAndUserId(publicID: String, userID: ID): Dice?
    suspend fun find(page: Page, count: Count): List<Dice>
    suspend fun search(query: SearchQuery): List<Dice>
    suspend fun deleteByPublicIdAndUserId(publicID: String, userId: ID)

    class Impl(
        private val diceFactory: DiceFactory,
        private val diceRepository: DiceRepository,
        private val searchIndexRepository: SearchIndexRepository,
        private val publicIDFactory: PublicIDFactory,
        private val diceMetricRepository: DiceMetricRepository,
        private val diceOwnerFactory: DiceOwnerFactory
    ) : DiceService {

        override suspend fun saveDice(name: Name, edges: Edges, userID: ID): PublicID {
            val dice = diceFactory.create(name, edges, userID)
            searchIndexRepository.add(dice)
            return publicIDFactory.fromID(dice.id)
        }

        override suspend fun findDiceByPublicID(publicID: String): Dice? {
            val id = publicIDFactory.fromStringOrNull(publicID)
                ?.let { it.toID() }
                ?: return null

            val metric = diceMetricRepository.forID(id)
            if (metric == null) {
                diceMetricRepository.create(id, Metric.Simple(1))
            } else {
                metric.addClick()
            }
            return diceRepository.oneOrNull(id)
        }

        override suspend fun findDiceByPublicIdAndUserId(publicID: String, userID: ID): Dice? {
            val diceOwner = diceOwnerFactory.create(userID)
            return publicIDFactory.fromStringOrNull(publicID)
                ?.toID()
                ?.let { diceOwner.findDice(it) }
        }

        override suspend fun find(page: Page, count: Count): List<Dice> {
            return diceRepository.flow()
                .toList()
                .map { it to diceMetricRepository.forIDOrZero(it.id) }
                .sortAndLimit(page, count)
        }

        override suspend fun search(query: SearchQuery): List<Dice> {
            val ids = searchIndexRepository.search(query.text)
                .map { it to diceMetricRepository.forIDOrZero(it) }
                .sortAndLimit(query.page, query.count)
            return diceRepository.list(ids)
        }

        private fun <T> List<Pair<T, Metric>>.sortAndLimit(page: Page, count: Count): List<T> {
            val offset = Offset(page, count)
            return sortedByDescending { it.second.clicks }
                .map { it.first }
                .drop(offset.toInt())
                .take(count.toInt())
        }

        override suspend fun deleteByPublicIdAndUserId(publicID: String, userId: ID) {
            val diceOwner = diceOwnerFactory.create(userId)
            publicIDFactory.fromStringOrNull(publicID)
                ?.toID()
                ?.let { diceOwner.deleteDice(it) }
        }
    }

    class Logging(private val service: DiceService) : DiceService {

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

}
