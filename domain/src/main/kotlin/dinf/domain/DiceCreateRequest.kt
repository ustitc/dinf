package dinf.domain

data class DiceCreateRequest(
    val name: Name,
    val ownerId: ID,
    val edges: List<String>
) {

    internal fun newDice(): Dice.New {
        return Dice.New(name = name, ownerId = ownerId, edges = edges)
    }

}
