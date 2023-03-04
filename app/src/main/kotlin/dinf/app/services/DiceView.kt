package dinf.app.services

data class DiceView(
    val id: PublicID,
    val name: String,
    val ownerId: PublicID,
    val edges: List<EdgeView>
)