package dinf.adapters

import dinf.domain.Name
import dinf.db.DiceEntity
import dinf.types.NBString
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBName(private val diceEntity: DiceEntity) : Name {

    override val nbString: NBString
        get() = NBString(diceEntity.name)

    override suspend fun change(new: NBString) = newSuspendedTransaction {
        diceEntity.name = new.toString()
    }
}
