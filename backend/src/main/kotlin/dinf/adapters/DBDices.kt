package dinf.adapters

import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.SerialNumber
import dinf.db.DiceEntity
import dinf.db.DiceTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DBDices : Dices {

    override fun flow(): Flow<Dice> = transaction {
        DiceEntity
            .all()
            .map { DBDice(it) }
            .asFlow()
    }

    override suspend fun oneOrNull(serialNumber: SerialNumber): Dice? {
        return newSuspendedTransaction {
            DiceEntity.findById(serialNumber.number)
        }?.let { DBDice(it) }
    }

    override suspend fun list(serials: List<SerialNumber>): List<Dice> {
        return newSuspendedTransaction {
            DiceEntity
                .find { DiceTable.id inList serials.map { it.number } }
                .map { DBDice(it) }
        }
    }
}
