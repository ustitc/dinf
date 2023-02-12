package dinf.adapters

import dinf.db.connection
import dinf.db.firstOrNull
import dinf.db.setPLong
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceRepository
import dinf.domain.ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class SqliteDiceRepository : DiceRepository {

    private val edgesSeparator = ";"

    override fun flow(): Flow<Dice> {
        val connection = connection()
        val statement = connection.prepareStatement(
            """
                    SELECT 
                        dices.id AS id, 
                        dices.name AS name, 
                        group_concat(edges.value, '$edgesSeparator') AS edges
                    FROM dices, edges 
                    WHERE dices.id = edges.dice
                    GROUP BY dices.id
                """.trimIndent()
        )
        val rs = statement.executeQuery()
        return flow<Dice> {
            rs.toSequence {
                SqliteDice(this, edgesSeparator)
            }.forEach { emit(it) }
        }.onCompletion {
            rs.close()
            statement.close()
            connection.close()
        }
    }

    override suspend fun oneOrNull(id: ID): Dice? {
        return transaction {
            val statement = prepareStatement(
                """
                    SELECT 
                        dices.id AS id, 
                        dices.name AS name, 
                        group_concat(edges.value, '$edgesSeparator') AS edges
                    FROM dices
                    LEFT JOIN edges ON dices.id = edges.dice
                    WHERE dices.id = ?
                    GROUP BY dices.id
                """.trimIndent()
            ).also { statement ->
                statement.setPLong(1, id.number)
            }
            val dice = statement.executeQuery().firstOrNull {
                SqliteDice(this, edgesSeparator)
            }
            statement.close()
            dice
        }
    }

    override suspend fun list(ids: List<ID>): List<Dice> {
        return transaction {
            val statement = prepareStatement(
                """
                SELECT 
                    dices.id AS id, 
                    dices.name AS name, 
                    group_concat(edges.value, '$edgesSeparator') AS edges
                FROM dices, edges 
                WHERE dices.id IN (${ids.joinToString(separator = ",") { "?" }}) AND dices.id = edges.dice
                GROUP BY dices.id
            """.trimIndent()
            ).also { statement ->
                ids.forEachIndexed { i, d ->
                    statement.setPLong(i + 1, d.number)
                }
            }
            val list = statement.executeQuery().toSequence {
                SqliteDice(this, edgesSeparator)
            }.toList()
            statement.close()
            list
        }
    }

    override suspend fun remove(dice: Dice) {
        transaction {
            prepareStatement("DELETE FROM dices WHERE id = ?").also {
                it.setPLong(1, dice.id.number)
            }.use { it.execute() }
        }
    }
}
