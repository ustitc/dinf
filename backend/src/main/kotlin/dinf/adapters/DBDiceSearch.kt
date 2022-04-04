package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceSearch
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.SearchQuery

class DBDiceSearch : DiceSearch {

    private val edgesSeparator = ";"

    override suspend fun invoke(query: SearchQuery): List<Dice> {
        return transaction {
            val statement = prepareStatement(
                """
                    SELECT 
                        dices.id AS id, 
                        dices.name AS name, 
                        group_concat(edges.value, '$edgesSeparator') AS edges
                    FROM dices, edges
                    WHERE dices.name LIKE ? AND dices.id = edges.dice
                    GROUP BY dices.id
                    LIMIT ? OFFSET ?
                """.trimIndent()
            ).also {
                it.setString(1, "${query.text}%")
                it.setInt(2, query.limit)
                it.setInt(3, query.offset)
            }

            val result = statement.executeQuery().toSequence {
                DBDice(this)
            }.toList()
            statement.close()
            result
        }
    }
}
