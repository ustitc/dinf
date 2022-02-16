package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.exposed.DiceEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBDiceDelete : DiceDelete {

    override suspend fun delete(dice: Dice) {
        newSuspendedTransaction {
            DiceEntity.findById(dice.serialNumber.number)?.delete()
        }
    }

}
