package dinf.backend

import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.SerialNumber
import dinf.exposed.DiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

class DBDices : Dices {

    override suspend fun flow(): Flow<Dice> = newSuspendedTransaction {
        DiceEntity
            .all()
            .map { DBDice(it) }
            .asFlow()
    }

    override suspend fun create(dice: Dice) {
        newSuspendedTransaction {
            val now = Instant.now()
            DiceEntity.new {
                name = dice.name.nbString.toString()
                edges = dice.edges.stringList.joinToString(separator = "\n")
                createdAt = now
                updatedAt = now
            }
        }
    }

    override suspend fun dice(serialNumber: SerialNumber): Dice? {
        return newSuspendedTransaction {
            DiceEntity.findById(serialNumber.number.toInt())
        }?.let { DBDice(it) }
    }
}
