package dinf.adapters

import dinf.domain.DiceDelete
import dinf.db.transaction

class DBDiceDelete : DiceDelete by DiceDelete({ dice ->
    transaction {
        prepareStatement("DELETE FROM dices WHERE id = ?").also {
            it.setLong(1, dice.id.number)
        }.use { it.execute() }
    }
})
