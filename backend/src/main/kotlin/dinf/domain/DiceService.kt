package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceService {

    suspend fun saveDice(name: Name, edges: Edges): PublicID

    suspend fun search(query: SearchQuery): List<Dice>

    suspend fun deleteByPublicID(publicID: PublicID)

    class Impl(
        private val diceFactory: DiceFactory,
        private val diceRepository: DiceRepository,
        private val searchIndexRepository: SearchIndexRepository,
        private val publicIDFactory: PublicIDFactory,
        private val diceMetricRepository: DiceMetricRepository
    ) : DiceService {

        override suspend fun saveDice(name: Name, edges: Edges): PublicID {
            val dice = diceFactory.create(name, edges)
            searchIndexRepository.add(dice)
            return publicIDFactory.fromID(dice.id)
        }

        override suspend fun search(query: SearchQuery): List<Dice> {
            val ids = searchIndexRepository.search(query.text)
                .map { it to diceMetricRepository.forIDOrZero(it) }
                .sortedByDescending { it.second.clicks }
                .map { it.first }
                .drop(query.offset)
                .take(query.limit)
            return diceRepository.list(ids)
        }

        override suspend fun deleteByPublicID(publicID: PublicID) {
            val toDelete = diceRepository.oneOrNull(publicID)
            if (toDelete != null) {
                diceRepository.remove(toDelete)
                diceMetricRepository.removeForID(toDelete.id)
            }
        }
    }

    class Logging(
        private val service: DiceService
    ) : DiceService {

        private val logger: Logger = LoggerFactory.getLogger(DiceService::class.java)

        override suspend fun saveDice(name: Name, edges: Edges): PublicID {
            val hashID = service.saveDice(name, edges)
            logger.info("Saved dice for id: ${hashID.toID()}")
            return hashID
        }

        override suspend fun search(query: SearchQuery): List<Dice> {
            val dices = service.search(query)
            logger.info("Found ${dices.size} dices for query: $query")
            return dices
        }

        override suspend fun deleteByPublicID(publicID: PublicID) {
            service.deleteByPublicID(publicID)
            logger.info("Deleted dice for id: ${publicID.toID()}")
        }
    }

    class Stub : DiceService {
        override suspend fun saveDice(name: Name, edges: Edges): PublicID = PublicID.Stub()
        override suspend fun search(query: SearchQuery): List<Dice> = emptyList()
        override suspend fun deleteByPublicID(publicID: PublicID) {}
    }

}
