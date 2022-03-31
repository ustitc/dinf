package dinf.adapters

import dinf.domain.Dice
import dinf.domain.DiceSearch
import dinf.db.toSequence
import dinf.db.transaction

class DBDiceSearch : DiceSearch {

    private val edgesSeparator = ";"

    override suspend fun invoke(text: String): List<Dice> {
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
                """.trimIndent()
            ).also { it.setString(1, "$text%") }

            val result = statement.executeQuery().toSequence {
                DBDice(this)
            }.toList()
            statement.close()
            result
        }
    }
}
