package dinf.adapters

import dinf.db.getPLong
import dinf.db.setPLong
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceSave
import dinf.domain.ID
import dinf.types.PLong
import java.sql.Connection

class DBDiceSave : DiceSave {

    override suspend fun invoke(dice: Dice): Dice {
        val id = transaction {
            val statement = prepareStatement(
                """
                    INSERT INTO dices (name, created_at, updated_at) 
                    VALUES (?, date('now'), date('now'))
                    RETURNING id, name
                """.trimIndent()
            ).also { it.setString(1, dice.name.print()) }

            val rs = statement.executeQuery()
            val id = rs.getPLong("id")!!
            dice.edges.toStringList().forEach { edge ->
                saveEdge(id, edge)
            }
            statement.close()
            rs.close()
            id
        }
        return DBDices().oneOrNull(ID(id)) ?: error("Dice was not saved")
    }

    private fun Connection.saveEdge(diceID: PLong, edge: String) {
        val statement = prepareStatement(
            """
                INSERT INTO edges (value, dice)
                VALUES (?, ?)
            """.trimIndent()
        ).also {
            it.setString(1, edge)
            it.setPLong(2, diceID)
        }
        statement.execute()
        statement.close()
    }
}
