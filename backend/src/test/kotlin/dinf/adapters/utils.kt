package dinf.adapters

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name
import dinf.domain.SerialNumber

suspend fun createDiceEntity(name: String = "test"): Dice {
    return DBDiceSave().create(
        Dice.New(
            name = Name.Stub(name),
            edges = Edges.Simple(listOf("1", "2", "3"))
        )
    )
}
