package dinf.domain

interface SearchIndexRepository {

    fun add(dice: Dice)

    fun search(query: SearchQuery): List<ID>

    class Stub(private val ids: List<ID> = emptyList()) : SearchIndexRepository {
        override fun add(dice: Dice) {
        }
        override fun search(query: SearchQuery): List<ID> = ids
    }

    class Empty : SearchIndexRepository by Stub(emptyList())

    class Error : SearchIndexRepository {
        override fun add(dice: Dice) {
            error("")
        }
        override fun search(query: SearchQuery): List<ID> {
            error("")
        }
    }

}