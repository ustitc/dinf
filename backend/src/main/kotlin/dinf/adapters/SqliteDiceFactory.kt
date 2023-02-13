package dinf.adapters

import dinf.db.first
import dinf.db.getPLong
import dinf.db.prepareStatement
import dinf.db.setPLong
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceFactory
import dinf.domain.Edges
import dinf.domain.ID
import dinf.domain.Name
import dinf.types.PLong
import java.sql.Connection

class SqliteDiceFactory : DiceFactory {

    override suspend fun create(name: Name, edges: Edges, ownerID: ID): Dice {
        val id = transaction {
            val diceID = saveDice(name)
            linkEdges(diceID, edges)
            linkOwner(diceID, ownerID)
            diceID
        }
        return SqliteDiceRepository().oneOrNull(ID(id)) ?: error("Dice was not saved")
    }

    private fun Connection.saveDice(name: Name): PLong {
        return prepareStatement(
            """
            INSERT INTO dices (name, created_at, updated_at) 
            VALUES (?, date('now'), date('now'))
            RETURNING id
            """
        ) {
            setString(1, name.print())

            executeQuery().first {
                getPLong("id")
            }
        }
    }

    private fun Connection.linkEdges(diceID: PLong, edges: Edges) {
        edges.toStringList().forEach { edge ->
            prepareStatement(
                """
                INSERT INTO edges (value, dice)
                VALUES (?, ?)
                """
            ) {
                setString(1, edge)
                setPLong(2, diceID)
                execute()
            }
        }
    }

    private fun Connection.linkOwner(diceID: PLong, ownerID: ID) {
        prepareStatement(
            """
            INSERT INTO dice_owners (dice, user)
            VALUES (?, ?)
            """
        ) {
            setPLong(1, diceID)
            setPLong(2, ownerID.number)
            execute()
        }
    }
}
