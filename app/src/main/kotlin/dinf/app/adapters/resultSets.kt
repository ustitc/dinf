package dinf.app.adapters

import dinf.app.db.getPLong
import dinf.domain.Edge
import dinf.domain.ID
import java.sql.ResultSet

fun ResultSet.toEdge(): Edge {
    return Edge(
        id = ID(getPLong("id")),
        value = getString("value"),
        diceId = ID(getPLong("dice"))
    )
}
