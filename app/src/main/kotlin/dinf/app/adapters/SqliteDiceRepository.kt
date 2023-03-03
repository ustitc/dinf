package dinf.app.adapters

import dinf.app.db.connection
import dinf.app.db.first
import dinf.app.db.firstOrNull
import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.sql
import dinf.app.db.toSequence
import dinf.app.db.transaction
import dinf.domain.Dice
import dinf.domain.DiceRepository
import dinf.domain.Edge
import dinf.domain.ID
import dinf.domain.Name
import dinf.types.PLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import java.sql.Connection
import java.sql.ResultSet

class SqliteDiceRepository : DiceRepository {

    override fun create(dice: Dice.New): Dice {
        val id = transaction {
            val diceID = saveDice(dice.name)
            linkOwner(diceID, dice.ownerId)
            diceID
        }
        return oneOrNull(ID(id)) ?: error("Dice was not saved")
    }

    private fun Connection.saveDice(name: Name): PLong {
        return prepareStatement(
            """
            INSERT INTO dices (name, created_at, updated_at) 
            VALUES (?, date('now'), date('now'))
            RETURNING id
            """
        ).use {
            it.setString(1, name.print())

            it.executeQuery().first {
                getPLong("id")
            }
        }
    }

    private fun Connection.linkOwner(diceID: PLong, ownerID: ID) {
        prepareStatement(
            """
            INSERT INTO dice_owners (dice, user)
            VALUES (?, ?)
            """
        ).use {
            it.setPLong(1, diceID)
            it.setPLong(2, ownerID.number)
            it.execute()
        }
    }

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

    override fun oneOrNull(id: ID): Dice? {
        return transaction {
            val statement = prepareStatement(
                """
                SELECT id, name, edges, owner 
                FROM dice_details
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

    override fun list(ids: List<ID>): List<Dice> {
        return transaction {
            val statement = prepareStatement(
                """
                SELECT id, name, edges, owner 
                FROM dice_details
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

    override fun update(dice: Dice) {
        sql(
            """
            UPDATE dices 
            SET name = ?,
                updated_at = date('now') 
            WHERE id = ?
            RETURNING id, name 
        """.trimIndent()
        ) {
            setString(1, dice.name.print())
            setPLong(2, dice.id.number)
            execute()
        }
    }

    override fun remove(dice: Dice) {
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
            edges = result.getString("edges")
                ?.split(EDGES_SEPARATOR)
                ?.map { Edge(ID.first(), it, id) }
                ?: emptyList(),
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
