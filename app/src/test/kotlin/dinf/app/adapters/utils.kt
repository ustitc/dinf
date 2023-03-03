package dinf.app.adapters

import dinf.app.db.first
import dinf.app.db.getPLong
import dinf.app.db.sql
import dinf.domain.Dice
import dinf.domain.DiceCreateRequest
import dinf.domain.DiceService
import dinf.domain.ID
import dinf.domain.Name

fun createDice(
    name: String = "test",
    edges: List<String> = listOf("1", "2", "3"),
    ownerID: ID = createUser()
): Dice {
    return DiceService(SqliteDiceRepository(), SqliteEdgeRepository()).createDice(
        DiceCreateRequest(
            name = Name(name),
            edges = edges,
            ownerId = ownerID
        )
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
