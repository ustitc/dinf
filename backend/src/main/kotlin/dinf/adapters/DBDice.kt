package dinf.adapters

import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name
import dinf.domain.SerialNumber
import java.sql.ResultSet

class DBDice private constructor(dice: Dice.Simple) : Dice by dice {

    override fun change(name: Name, edges: Edges) {
        transaction {
            prepareStatement("UPDATE dices SET name = ? WHERE id = ?")
                .also {
                    it.setString(1, name.print())
                    it.setLong(2, serialNumber.number)
                }.use { it.execute() }

            prepareStatement("DELETE FROM edges WHERE dice = ?")
                .also { it.setLong(1, serialNumber.number) }
                .use { it.execute() }

            edges.stringList.forEach { value ->
                prepareStatement("INSERT INTO edges (value, dice) VALUES (?, ?)")
                    .also {
                        it.setString(1, value)
                        it.setLong(2, serialNumber.number)
                    }.use { it.execute() }
            }
        }
    }

    constructor(result: ResultSet, edgesSeparator: String) : this(
        SerialNumber.Simple(result.getLong("id"))
            .let {
                Dice.Simple(
                    serialNumber = it,
                    name = Name(result.getString("name")),
                    edges = Edges.Simple(result.getString("edges").split(edgesSeparator))
                )
            }
    )
}
