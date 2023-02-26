package dinf.app.adapters

import dinf.app.db.setPLong
import dinf.app.db.transaction
import dinf.domain.Edge
import dinf.domain.Edges
import dinf.domain.ID

class SqliteEdges(private val diceId: ID, private val list: List<String>) : Edges {

    override fun asEdgeList(): List<Edge> = list.map { Edge(ID.first(), it) }

    override fun replaceAll(list: List<Edge>) {
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
}
