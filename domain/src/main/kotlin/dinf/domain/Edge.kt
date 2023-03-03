package dinf.domain

data class Edge(val id: ID, val value: String, val diceId: ID) {

    data class New(val value: String, val diceId: ID)

}
