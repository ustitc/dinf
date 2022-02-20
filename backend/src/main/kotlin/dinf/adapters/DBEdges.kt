package dinf.adapters

import dinf.domain.Edges
import dinf.db.DiceEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBEdges(private val diceEntity: DiceEntity) : Edges {

    override val stringList: List<String>
        get() = diceEntity.edges.lines()

    override suspend fun change(new: Edges) {
        newSuspendedTransaction {
            diceEntity.edges = new.stringList.joinToString("\n")
        }
    }
}
