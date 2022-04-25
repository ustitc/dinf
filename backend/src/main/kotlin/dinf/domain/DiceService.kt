package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceService {

    suspend fun saveDice(name: Name, edges: Edges): HashID

    suspend fun search(query: SearchQuery): List<Dice>

    suspend fun deleteByHashID(hashID: HashID)

    class Impl(
        private val diceFactory: DiceFactory,
        private val diceRepository: DiceRepository,
        private val searchIndexRepository: SearchIndexRepository,
        private val hashIDFactory: HashIDFactory,
        private val diceMetricRepository: DiceMetricRepository
    ) : DiceService {

        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            val dice = diceFactory.create(name, edges)
            searchIndexRepository.add(dice)
            return hashIDFactory.fromID(dice.id)
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

        override suspend fun deleteByHashID(hashID: HashID) {
            val toDelete = diceRepository.oneOrNull(hashID)
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

        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            val hashID = service.saveDice(name, edges)
            logger.info("Saved dice for id: ${hashID.toID()}")
            return hashID
        }

        override suspend fun search(query: SearchQuery): List<Dice> {
            val dices = service.search(query)
            logger.info("Found ${dices.size} dices for query: $query")
            return dices
        }

        override suspend fun deleteByHashID(hashID: HashID) {
            service.deleteByHashID(hashID)
            logger.info("Deleted dice for id: ${hashID.toID()}")
        }
    }

    class Stub : DiceService {
        override suspend fun saveDice(name: Name, edges: Edges): HashID = HashID.Stub()
        override suspend fun search(query: SearchQuery): List<Dice> = emptyList()
        override suspend fun deleteByHashID(hashID: HashID) {}
    }

}
