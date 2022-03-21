package dinf.adapters

import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceSave
import dinf.domain.SerialNumber
import java.sql.Connection

class DBDiceSave : DiceSave {

    override suspend fun create(dice: Dice): Dice {
        val id = transaction {
            val statement = prepareStatement(
                """
                    INSERT INTO dices (name, created_at, updated_at) 
                    VALUES (?, date('now'), date('now'))
                    RETURNING id, name
                """.trimIndent()
            ).also { it.setString(1, dice.name.nbString.toString()) }

            val rs = statement.executeQuery()
            val id = rs.getLong("id")
            dice.edges.stringList.forEach { edge ->
                saveEdge(id, edge)
            }
            statement.close()
            rs.close()
            id
        }
        return DBDices().oneOrNull(SerialNumber.Simple(id)) ?: error("Dice was not saved")
    }

    private fun Connection.saveEdge(diceID: Long, edge: String) {
        val statement = prepareStatement(
            """
                INSERT INTO edges (value, dice)
                VALUES (?, ?)
            """.trimIndent()
        ).also {
            it.setString(1, edge)
            it.setLong(2, diceID)
        }
        statement.execute()
        statement.close()
    }
}
