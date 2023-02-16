package dinf.adapters

import dinf.db.firstOrNull
import dinf.db.setPLong
import dinf.db.sql
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceOwner
import dinf.domain.DiceOwnerFactory
import dinf.domain.ID

class SqliteDiceOwner(override val id: ID) : DiceOwner {

    override fun findDice(diceId: ID): Dice? {
        return sql(
            """
            SELECT 
                dices.id, 
                dices.name,
                group_concat(edges.value, '${SqliteDice.EDGES_SEPARATOR}') AS edges
            FROM dices
            JOIN dice_owners ON dices.id = dice_owners.dice
            JOIN edges ON dices.id = edges.dice
            WHERE dice_owners.dice = ? 
            AND dice_owners.user = ?
            GROUP BY dices.id
            """.trimIndent()
        ) {
            setPLong(1, diceId.number)
            setPLong(2, id.number)

            executeQuery().firstOrNull {
                SqliteDice.fromResultSet(this)
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

    companion object : DiceOwnerFactory {
        override fun create(userID: ID): DiceOwner {
            return SqliteDiceOwner(userID)
        }
    }
}
