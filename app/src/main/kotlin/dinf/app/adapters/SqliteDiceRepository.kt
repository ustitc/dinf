package dinf.app.adapters

import dinf.app.db.connection
import dinf.app.db.firstOrNull
import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.sql
import dinf.app.db.toSequence
import dinf.app.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceRepository
import dinf.domain.ID
import dinf.domain.Name
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import java.sql.ResultSet

class SqliteDiceRepository : DiceRepository {

    override fun flow(): Flow<Dice> {
        val connection = connection()
        val statement = connection.prepareStatement("SELECT id, name, edges, owner FROM dice_details")
        val rs = statement.executeQuery()
        return flow {
            rs.toSequence {
                fromResultSet(this)
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
                SELECT id, name, edges, owner FROM dice_details
                WHERE id = ?
                """.trimIndent()
            ).also { statement ->
                statement.setPLong(1, id.number)
            }
            val dice = statement.executeQuery().firstOrNull {
                fromResultSet(this)
            }
            statement.close()
            dice
        }
    }

    override suspend fun list(ids: List<ID>): List<Dice> {
        return transaction {
            val statement = prepareStatement(
                """
                SELECT id, name, edges, owner FROM dice_details
                WHERE id IN (${ids.joinToString(separator = ",") { "?" }}) 
                """.trimIndent()
            ).also { statement ->
                ids.forEachIndexed { i, d ->
                    statement.setPLong(i + 1, d.number)
                }
            }
            val list = statement.executeQuery().toSequence {
                fromResultSet(this)
            }.toList()
            statement.close()
            list
        }
    }

    override suspend fun update(dice: Dice) {
        sql(
            """
            UPDATE dices SET
            name = ?
            RETURNING id, name 
        """.trimIndent()
        ) {
            setString(1, dice.name.print())
            execute()
        }
    }

    override suspend fun remove(dice: Dice) {
        transaction {
            prepareStatement("DELETE FROM dices WHERE id = ?").also {
                it.setPLong(1, dice.id.number)
            }.use { it.execute() }
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
                    ?.split(EDGES_SEPARATOR)
                    ?: emptyList()
            ),
            ownerId = ID(result.getPLong("owner"))
        )
    }

    override fun search(text: String): List<Dice> {
        return transaction {
            prepareStatement(
                """
                    SELECT id, name, edges, owner FROM dice_details
                    WHERE name LIKE ? 
                """.trimIndent()
            ).also {
                it.setString(1, "${text}%")
            }.use {
                it.executeQuery().toSequence {
                    fromResultSet(this)
                }.toList()
            }
        }
    }

    companion object {
        const val EDGES_SEPARATOR = ";"
    }

}
