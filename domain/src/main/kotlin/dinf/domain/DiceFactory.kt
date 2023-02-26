package dinf.domain

interface DiceFactory {

    suspend fun create(name: Name, edges: List<Edge>, ownerID: ID): Dice

}
