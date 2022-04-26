package dinf.adapters

import dinf.db.getPLong
import dinf.db.setPLong
import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name
import dinf.domain.ID
import java.sql.ResultSet

class DBDice private constructor(dice: Dice.Simple) : Dice by dice {

    constructor(result: ResultSet, edgesSeparator: String) : this(
        ID(result.getPLong("id")!!)
            .let {
                Dice.Simple(
                    id = it,
                    name = Name(result.getString("name")),
                    edges = Edges(
                        result.getString("edges")?.split(edgesSeparator) ?: emptyList()
                    )
                )
            }
    )

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
}
