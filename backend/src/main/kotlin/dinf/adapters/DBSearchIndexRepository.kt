package dinf.adapters

import dinf.db.getPLongOrNull
import dinf.domain.Dice
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.ID
import dinf.domain.SearchIndexRepository

class DBSearchIndexRepository : SearchIndexRepository {

    override fun add(dice: Dice) {
    }

    override fun search(text: String): List<ID> {
        return transaction {
            val statement = prepareStatement(
                """
                    SELECT dices.id AS id 
                    FROM dices
                    WHERE dices.name LIKE ?
                """.trimIndent()
            ).also {
                it.setString(1, "${text}%")
            }

            val result = statement.executeQuery().toSequence {
                getPLongOrNull("id")!!
            }.toList().map { ID(it) }
            statement.close()
            result
        }
    }
}
