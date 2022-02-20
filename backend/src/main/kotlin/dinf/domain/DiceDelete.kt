package dinf.domain

interface DiceDelete {

    suspend fun delete(dice: Dice)

    class Stub : DiceDelete {

        override suspend fun delete(dice: Dice) {
        }
    }

}
