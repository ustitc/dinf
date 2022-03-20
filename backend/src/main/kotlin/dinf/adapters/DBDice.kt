package dinf.adapters

import dinf.domain.Dice
import dinf.domain.SerialNumber
import java.sql.ResultSet

class DBDice private constructor(dice: Dice.Simple) : Dice by dice {

    constructor(result: ResultSet) : this(
        SerialNumber.Simple(result.getLong("id"))
            .let {
                Dice.Simple(
                    serialNumber = it,
                    name = DBName(it),
                    edges = DBEdges(it)
                )
            }
    )
}
