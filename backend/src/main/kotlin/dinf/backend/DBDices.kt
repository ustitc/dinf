package dinf.backend

import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import dinf.exposed.DiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBDices : Dices {

    override suspend fun flow(): Flow<Dice> = newSuspendedTransaction {
        DiceEntity
            .all()
            .map { DBDice(it) }
            .asFlow()
    }

    override suspend fun create(edges: Edges) {
        TODO("Not yet implemented")
    }
}
