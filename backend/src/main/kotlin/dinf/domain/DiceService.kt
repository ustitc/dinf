package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceService {

    suspend fun saveDice(name: Name, edges: Edges): HashID

    suspend fun search(query: SearchQuery): List<Dice>

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
    }

    class Stub : DiceService {
        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            return HashID.Stub()
        }
        override suspend fun search(query: SearchQuery): List<Dice> = emptyList()
    }

}
