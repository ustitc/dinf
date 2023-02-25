package dinf.adapters

import dinf.db.first
import dinf.db.getPLong
import dinf.db.sql
import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.ID
import dinf.domain.Name

suspend fun createDice(
    name: String = "test",
    edges: List<String> = listOf("1", "2", "3"),
    ownerID: ID = createUser()
): Dice {
    return SqliteDiceFactory().create(
        name = Name(name),
        edges = Edges(edges),
        ownerID = ownerID
    )
}

fun createUser(): ID {
    return sql("""INSERT INTO users(name) VALUES(?) RETURNING id""") {
        setString(1, "Happy User")
        executeQuery().first {
            getPLong("id")
        }
    }.let { ID(it) }
}
