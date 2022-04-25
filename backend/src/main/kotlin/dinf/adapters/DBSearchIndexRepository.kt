package dinf.adapters

import dinf.db.getPLong
import dinf.domain.Dice
import dinf.db.toSequence
import dinf.db.transaction
import dinf.domain.ID
import dinf.domain.SearchIndexRepository
import dinf.domain.SearchQuery

class DBSearchIndexRepository : SearchIndexRepository {

    override fun add(dice: Dice) {
    }

    override fun search(query: SearchQuery): List<ID> {
        return transaction {
            val statement = prepareStatement(
                """
                    SELECT dices.id AS id 
                    FROM dices
                    WHERE dices.name LIKE ?
                    LIMIT ? OFFSET ?
                """.trimIndent()
            ).also {
                it.setString(1, "${query.text}%")
                it.setInt(2, query.limit)
                it.setInt(3, query.offset)
            }

            val result = statement.executeQuery().toSequence {
                getPLong("id")!!
            }.toList().map { ID(it) }
            statement.close()
            result
        }
    }
}
