package dinf.domain

interface DiceFactory {

    suspend fun create(name: Name, edges: Edges, ownerID: ID): Dice

    class Stub(private val dice: Dice = Dice.Stub()) : DiceFactory {
        override suspend fun create(name: Name, edges: Edges, ownerID: ID): Dice = dice
    }
}
