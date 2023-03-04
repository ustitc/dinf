package dinf.app.adapters

import dinf.app.db.first
import dinf.app.db.firstOrNull
import dinf.app.db.setPLong
import dinf.app.db.sql
import dinf.app.db.transaction
import dinf.domain.Edge
import dinf.domain.EdgeRepository
import dinf.domain.ID

class SqliteEdgeRepository : EdgeRepository {

    override fun update(edge: Edge) {
        transaction {
            prepareStatement(
                """
                UPDATE edges 
                SET value = ?, 
                    dice = ?
                WHERE id = ?
                """.trimIndent()
            ).also {
                it.setString(1, edge.value)
                it.setPLong(2, edge.diceId.toPLong())
                it.setPLong(3, edge.id.toPLong())

            }.use { it.execute() }
        }
    }

    override fun createAll(list: List<Edge.New>): List<Edge> {
        return transaction {
            list.map { edge ->
                prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?) RETURNING id, value, dice")
                    .also {
                        it.setString(1, edge.value)
                        it.setPLong(2, edge.diceId.toPLong())
                    }.use {
                        it.executeQuery()
                            .first {
                                toEdge()
                            }
                    }
            }
        }
    }

    override fun create(edge: Edge.New): Edge {
        return sql("INSERT INTO edges (value, dice) VALUES (?, ?) RETURNING id, value, dice") {
            setString(1, edge.value)
            setPLong(2, edge.diceId.toPLong())
            executeQuery().first {
                toEdge()
            }
        }
    }

    override fun oneOrNull(id: ID): Edge? {
        return sql("SELECT id, value, dice FROM edges where id = ?") {
            setPLong(1, id.toPLong())
            executeQuery().firstOrNull {
                toEdge()
            }
        }
    }

    override fun deleteAllByDiceId(diceId: ID) {
        transaction {
            prepareStatement("DELETE FROM edges WHERE dice = ?")
                .also { it.setPLong(1, diceId.toPLong()) }
                .use { it.execute() }
        }
    }
}
