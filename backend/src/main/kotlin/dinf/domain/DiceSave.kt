package dinf.domain

interface DiceSave {

    suspend fun create(dice: Dice): Dice

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceSave {

        override suspend fun create(dice: Dice): Dice {
            list.add(dice)
            return dice
        }
    }

}
