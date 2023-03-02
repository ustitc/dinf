package dinf.domain

interface EdgeRepository {

    fun replaceAll(diceId: ID, list: List<Edge>)

}