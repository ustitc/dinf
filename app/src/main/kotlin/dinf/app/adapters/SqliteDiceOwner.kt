package dinf.app.adapters

import dinf.app.db.firstOrNull
import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.sql
import dinf.app.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceOwner
import dinf.domain.DiceOwnerFactory
import dinf.domain.ID
import dinf.domain.Name
import java.sql.ResultSet

class SqliteDiceOwner(override val id: ID) : DiceOwner {

    override fun findDice(diceId: ID): Dice? {
        return sql(
            """
            SELECT 
                dices.id, 
                dices.name,
                COALESCE(group_concat(edges.value, '${SqliteDiceRepository.EDGES_SEPARATOR}'), '') AS edges
            FROM dices
            JOIN dice_owners ON dices.id = dice_owners.dice
            LEFT JOIN edges ON dices.id = edges.dice
            WHERE dice_owners.dice = ? 
            AND dice_owners.user = ?
            GROUP BY dices.id
            """.trimIndent()
        ) {
            setPLong(1, diceId.number)
            setPLong(2, id.number)

            executeQuery().firstOrNull {
                fromResultSet(this)
            }
        }
    }

    override fun deleteDice(diceId: ID) {
        transaction {
            val updatedRows = prepareStatement(
                """
                DELETE FROM dice_owners 
                WHERE dice_owners.dice = ? AND dice_owners.user = ?
                """.trimIndent()
            ).use {
                it.setPLong(1, diceId.number)
                it.setPLong(2, id.number)
                it.executeUpdate()
            }

            if (updatedRows > 0) {
                prepareStatement("DELETE FROM dices WHERE dices.id = ?").use {
                    it.setPLong(1, diceId.number)
                    it.execute()
                }
            }
        }
    }

    private fun fromResultSet(result: ResultSet): Dice {
        val id = ID(result.getPLong("id"))
        return Dice(
            id = id,
            name = Name(result.getString("name")),
            edges = SqliteEdges(
                diceId = id,
                list = result.getString("edges")
                    ?.split(SqliteDiceRepository.EDGES_SEPARATOR)
                    ?: emptyList()
            )
        )
    }

    companion object : DiceOwnerFactory {
        override fun create(userID: ID): DiceOwner {
            return SqliteDiceOwner(userID)
        }
    }
}
