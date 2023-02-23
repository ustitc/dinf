package dinf.domain

interface DiceFactory {

    suspend fun create(name: Name, edges: Edges, ownerID: ID): Dice

}
