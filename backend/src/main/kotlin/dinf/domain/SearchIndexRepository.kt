package dinf.domain

interface SearchIndexRepository {

    fun add(dice: Dice)

    fun search(text: String): List<ID>

    class Stub(private val ids: List<ID> = emptyList()) : SearchIndexRepository {
        override fun add(dice: Dice) {
        }
        override fun search(text: String): List<ID> = ids
    }

    class Empty : SearchIndexRepository by Stub(emptyList())

    class Error : SearchIndexRepository {
        override fun add(dice: Dice) {
            error("")
        }
        override fun search(text: String): List<ID> {
            error("")
        }
    }

}