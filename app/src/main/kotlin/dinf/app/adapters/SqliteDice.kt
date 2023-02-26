package dinf.app.adapters

import dinf.app.db.getPLong
import dinf.app.db.setPLong
import dinf.app.db.transaction
import dinf.domain.Dice
import dinf.domain.ID
import dinf.domain.Name
import java.sql.ResultSet

class SqliteDice private constructor(dice: Dice.Simple) : Dice by dice {

    override fun rename(name: Name) {
        transaction {
            prepareStatement("UPDATE dices SET name = ? WHERE id = ?")
                .also {
                    it.setString(1, name.print())
                    it.setPLong(2, id.number)
                }.use { it.execute() }
        }
    }

    companion object {

        const val EDGES_SEPARATOR = ";"

        fun fromResultSet(result: ResultSet): SqliteDice {
            val id = ID(result.getPLong("id"))
            val dice = Dice.Simple(
                id = id,
                name = Name(result.getString("name")),
                edges = SqliteEdges(
                    diceId = id,
                    list = result.getString("edges")
                        ?.split(EDGES_SEPARATOR)
                        ?: emptyList()
                )
            )
            return SqliteDice(dice)
        }
    }
}
