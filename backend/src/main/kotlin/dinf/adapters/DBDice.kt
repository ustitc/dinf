package dinf.adapters

import dinf.db.transaction
import dinf.domain.Dice
import dinf.domain.Name
import dinf.domain.SerialNumber
import java.sql.ResultSet

class DBDice private constructor(dice: Dice.Simple) : Dice by dice {

    override fun change(name: Name) {
        transaction {
            prepareStatement("UPDATE dices SET name = ? WHERE id = ?")
                .also {
                    it.setString(1, name.print())
                    it.setLong(2, serialNumber.number)
                }.use { it.execute() }
        }
    }

    constructor(result: ResultSet) : this(
        SerialNumber.Simple(result.getLong("id"))
            .let {
                Dice.Simple(
                    serialNumber = it,
                    name = Name(result.getString("name")),
                    edges = DBEdges(it)
                )
            }
    )
}
