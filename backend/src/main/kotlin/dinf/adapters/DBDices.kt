package dinf.adapters

import dinf.db.connection
import dinf.db.firstOrNull
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.SerialNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class DBDices : Dices {

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
                DBDice(this)
            }.forEach { emit(it) }
        }.onCompletion {
            rs.close()
            statement.close()
            connection.close()
        }
    }

    override suspend fun oneOrNull(serialNumber: SerialNumber): Dice? {
        return transaction {
            val statement = prepareStatement(
                """
                    SELECT 
                        dices.id AS id, 
                        dices.name AS name, 
                        group_concat(edges.value, '$edgesSeparator') AS edges
                    FROM dices, edges 
                    WHERE dices.id = ? AND dices.id = edges.dice
                    GROUP BY dices.id
                """.trimIndent()
            ).also { statement ->
                statement.setLong(1, serialNumber.number)
            }
            val dice = statement.executeQuery().firstOrNull {
                DBDice(this)
            }
            statement.close()
            dice
        }
    }

    override suspend fun list(serials: List<SerialNumber>): List<Dice> {
        return transaction {
            val statement = prepareStatement(
                """
                SELECT 
                    dices.id AS id, 
                    dices.name AS name, 
                    group_concat(edges.value, '$edgesSeparator') AS edges
                FROM dices, edges 
                WHERE dices.id IN (${serials.joinToString(separator = ",") { "?" }}) AND dices.id = edges.dice
                GROUP BY dices.id
            """.trimIndent()
            ).also { statement ->
                serials.forEachIndexed { i, d ->
                    statement.setLong(i + 1, d.number)
                }
            }
            val list = statement.executeQuery().toSequence {
                DBDice(this)
            }.toList()
            statement.close()
            list
        }
    }
}
