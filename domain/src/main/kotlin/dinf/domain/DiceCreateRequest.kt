package dinf.domain

data class DiceCreateRequest(
    val name: Name,
    val ownerId: ID,
    val edges: List<String>
) {

    internal fun newDice(): Dice.New {
        return Dice.New(name = name, ownerId = ownerId)
    }

    internal fun newEdges(diceID: ID): List<Edge.New> {
        return edges.map { Edge.New(it, diceID) }
    }

}
