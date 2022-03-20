package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.db.transaction

class DBDiceDelete : DiceDelete {

    override suspend fun delete(dice: Dice) {
        transaction {
            prepareStatement("DELETE FROM dices WHERE id = ?").also {
                it.setLong(1, dice.serialNumber.number)
            }.use { it.execute() }
            prepareStatement("DELETE FROM edges WHERE dice = ?").also {
                it.setLong(1, dice.serialNumber.number)
            }.use { it.execute() }
        }
    }

}
