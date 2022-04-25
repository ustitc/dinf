package dinf.domain

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
            val ids = searchIndexRepository.search(query)
            return diceRepository.list(ids)
                .map { it to diceMetricRepository.forDiceOrZero(it) }
                .sortedByDescending { it.second.clicks }
                .map { it.first }
        }
    }

    class Stub : DiceService {
        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            return HashID.Stub()
        }
        override suspend fun search(query: SearchQuery): List<Dice> = emptyList()
    }

}
