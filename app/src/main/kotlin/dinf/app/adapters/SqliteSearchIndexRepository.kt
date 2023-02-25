package dinf.app.adapters

import dinf.app.db.getPLongOrNull
import dinf.domain.Dice
import dinf.app.db.toSequence
import dinf.app.db.transaction
import dinf.domain.ID
import dinf.domain.SearchIndexRepository

class SqliteSearchIndexRepository : SearchIndexRepository {

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
