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

    override suspend fun create(dice: Dice): Dice {
        val entity = newSuspendedTransaction {
            val now = Instant.now()
            DiceEntity.new {
                name = dice.name.nbString.toString()
                edges = dice.edges.stringList.joinToString(separator = "\n")
                createdAt = now
                updatedAt = now
            }
        }
        return DBDice(entity)
    }

    override suspend fun diceOrNull(serialNumber: SerialNumber): Dice? {
        return newSuspendedTransaction {
            DiceEntity.findById(serialNumber.number)
        }?.let { DBDice(it) }
    }

    override suspend fun delete(dice: Dice) {
        newSuspendedTransaction {
            DiceEntity.findById(dice.serialNumber.number)?.delete()
        }
    }
}
