package dinf.domain

interface SearchIndexRepository {

    fun add(dice: Dice)

    class Stub : SearchIndexRepository {
        override fun add(dice: Dice) {
        }
    }

}