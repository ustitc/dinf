package dinf.adapters

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name

suspend fun createDiceEntity(name: String = "test", edges: List<String> = listOf("1", "2", "3")): Dice {
    return DBDiceFactory().create(
        name = Name(name),
        edges = Edges(edges)
    )
}
