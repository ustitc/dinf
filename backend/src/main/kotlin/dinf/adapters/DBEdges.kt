package dinf.adapters

import dinf.domain.Edges
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.SerialNumber

class DBEdges(private val diceSerial: SerialNumber) : Edges {

    override val stringList: List<String>
        get() = transaction {
            prepareStatement("SELECT value FROM edges WHERE dice = ?")
                .also { it.setLong(1, diceSerial.number) }
                .use {
                    it.executeQuery()
                        .toSequence {
                            getString(1)
                        }.toList()
                }

        }

    override suspend fun change(new: Edges) {
        transaction {
            prepareStatement("DELETE FROM edges WHERE dice = ?")
                .also { it.setLong(1, diceSerial.number) }
                .use { it.execute() }

            new.stringList.forEach { value ->
                prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?)")
                    .also {
                        it.setString(1, value)
                        it.setLong(2, diceSerial.number)
                    }.use { it.execute() }
            }
        }
    }
}
