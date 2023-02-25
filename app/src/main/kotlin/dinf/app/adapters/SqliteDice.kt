package dinf.app.adapters

import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.transaction
import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name
import dinf.domain.ID
import java.sql.ResultSet

class SqliteDice private constructor(dice: Dice.Simple) : Dice by dice {

    override fun change(name: Name, edges: Edges) {
        transaction {
            prepareStatement("UPDATE dices SET name = ? WHERE id = ?")
                .also {
                    it.setString(1, name.print())
                    it.setPLong(2, id.number)
                }.use { it.execute() }

            prepareStatement("DELETE FROM edges WHERE dice = ?")
                .also { it.setPLong(1, id.number) }
                .use { it.execute() }

            edges.toStringList().forEach { value ->
                prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?)")
                    .also {
                        it.setString(1, value)
                        it.setPLong(2, id.number)
                    }.use { it.execute() }
            }
        }
    }

    companion object {

        const val EDGES_SEPARATOR = ";"

        fun fromResultSet(result: ResultSet): SqliteDice {
            val id = ID(result.getPLong("id"))
            val dice = Dice.Simple(
                id = id,
                name = Name(result.getString("name")),
                edges = Edges(
                    result.getString("edges")?.split(EDGES_SEPARATOR) ?: emptyList()
                )
            )
            return SqliteDice(dice)
        }
    }
}
