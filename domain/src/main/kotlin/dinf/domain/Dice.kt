package dinf.domain

data class Dice(
    val id: ID,
    val name: Name,
    val edges: Edges,
    val ownerId: ID
)
