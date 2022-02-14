package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceSave
import dinf.exposed.DiceEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

class DBDiceSave : DiceSave {

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
}
