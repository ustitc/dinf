package dinf.domain

data class DiceUpdateRequest(
    val diceId: ID,
    val ownerID: ID,
    val toUpdate: ToUpdate
) {

    data class ToUpdate(val name: Name, val edges: List<String> = emptyList())

    internal val newEdges: List<Edge.New> get() {
        return toUpdate.edges.map { Edge.New(it, diceId) }
    }

}
