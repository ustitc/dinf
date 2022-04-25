package dinf.domain

interface DiceService {

    suspend fun saveDice(name: Name, edges: Edges): HashID

    class Impl(
        private val diceFactory: DiceFactory,
        private val searchIndexRepository: SearchIndexRepository,
        private val hashIDFactory: HashIDFactory
    ) : DiceService {

        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            val dice = diceFactory.create(name, edges)
            searchIndexRepository.add(dice)
            return hashIDFactory.fromID(dice.id)
        }
    }

    class Stub : DiceService {
        override suspend fun saveDice(name: Name, edges: Edges): HashID {
            return HashID.Stub()
        }
    }

}
