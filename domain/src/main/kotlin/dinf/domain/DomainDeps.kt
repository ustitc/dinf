package dinf.domain

interface DomainDeps {

    fun diceRepository(): DiceRepository

    fun edgeRepository(): EdgeRepository

    fun diceService(): DiceService {
        return DiceService(
            diceRepository = diceRepository(),
            edgeRepository = edgeRepository()
        )
    }
}
