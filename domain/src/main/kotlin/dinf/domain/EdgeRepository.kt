package dinf.domain

interface EdgeRepository {

    fun update(edge: Edge)

    fun create(edge: Edge.New): Edge

    fun createAll(list: List<Edge.New>): List<Edge>

    fun oneOrNull(id: ID): Edge?

    fun deleteAllByDiceId(diceId: ID)

}