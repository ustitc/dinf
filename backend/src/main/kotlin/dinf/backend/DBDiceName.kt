package dinf.backend

import dinf.domain.Dice
import dinf.domain.Name
import dinf.exposed.DiceEntity
import dinf.types.NBString
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBDiceName(
    private val diceEntity: DiceEntity,
    override val nbString: NBString = NBString(diceEntity.name)
) : Name<Dice> {

    override suspend fun change(new: NBString) = newSuspendedTransaction {
        diceEntity.name = new.toString()
    }
}
