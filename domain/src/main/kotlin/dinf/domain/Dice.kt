package dinf.domain

data class Dice(
    val id: ID,
    val name: Name,
    val edges: List<Edge>,
    val ownerId: ID
) {

    data class New(
        val name: Name,
        val ownerId: ID
    )

}
