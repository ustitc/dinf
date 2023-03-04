package dinf.app.adapters

import dinf.domain.DiceRepository
import dinf.domain.DomainDeps
import dinf.domain.EdgeRepository

class SqliteDomainDeps : DomainDeps {

    private val diceRepository: DiceRepository = SqliteDiceRepository()
    private val edgeRepository: EdgeRepository = SqliteEdgeRepository()

    override fun diceRepository(): DiceRepository {
        return diceRepository
    }

    override fun edgeRepository(): EdgeRepository {
        return edgeRepository
    }
}