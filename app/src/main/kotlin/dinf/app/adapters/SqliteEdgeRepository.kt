package dinf.app.adapters

import dinf.app.db.first
import dinf.app.db.firstOrNull
import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.sql
import dinf.app.db.transaction
import dinf.domain.Edge
import dinf.domain.EdgeRepository
import dinf.domain.ID
import java.sql.ResultSet

class SqliteEdgeRepository : EdgeRepository {

    override fun replaceAll(diceId: ID, list: List<Edge>) {
        transaction {
            prepareStatement("DELETE FROM edges WHERE dice = ?")
                .also { it.setPLong(1, diceId.number) }
                .use { it.execute() }

            list.forEach { edge ->
                prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?)")
                    .also {
                        it.setString(1, edge.value)
                        it.setPLong(2, diceId.number)
                    }.use { it.execute() }
            }
        }
    }

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
                it.setPLong(2, edge.diceId.number)
                it.setLong(3, edge.id.toLong())

            }.use { it.execute() }
        }
    }

    override fun createAll(list: List<Edge.New>): List<Edge> {
        return list.map {
            create(it)
        }
    }

    override fun create(edge: Edge.New): Edge {
        return sql("INSERT INTO edges (value, dice) VALUES (?, ?) RETURNING id, value, dice") {
            setString(1, edge.value)
            setPLong(2, edge.diceId.number)
            executeQuery().first {
                toEdge(this)
            }
        }
    }

    override fun oneOrNull(id: ID): Edge? {
        return sql("SELECT id, value, dice FROM edges where id = ?") {
            setPLong(1, id.number)
            executeQuery().firstOrNull {
                toEdge(this)
            }
        }
    }

    private fun toEdge(rs: ResultSet): Edge {
        return Edge(
            id = ID(rs.getPLong("id")),
            value = rs.getString("value"),
            diceId = ID(rs.getPLong("dice"))
        )
    }
}
