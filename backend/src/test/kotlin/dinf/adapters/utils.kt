package dinf.adapters

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name

suspend fun createDiceEntity(name: String = "test"): Dice {
    return DBDiceFactory().create(
        name = Name(name),
        edges = Edges(listOf("1", "2", "3"))
    )
}
