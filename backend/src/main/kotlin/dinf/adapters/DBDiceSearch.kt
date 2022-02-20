package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceSearch
import dinf.db.DiceEntity
import dinf.db.DiceTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBDiceSearch : DiceSearch {

    override suspend fun forText(text: String): List<Dice> {
        return newSuspendedTransaction {
            DiceEntity
                .find { DiceTable.name like text }
                .map { DBDice(it) }
        }
    }
}
