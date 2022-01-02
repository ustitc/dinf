package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Dices {

    suspend fun create(dice: Dice): Dice

    suspend fun flow(): Flow<Dice>

    suspend fun diceOrNull(serialNumber: SerialNumber): Dice?

    suspend fun delete(dice: Dice)

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : Dices {

        override suspend fun create(dice: Dice): Dice {
            list.add(dice)
            return dice
        }

        override suspend fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun diceOrNull(serialNumber: SerialNumber): Dice? {
            return list.firstOrNull { it.serialNumber.number == serialNumber.number }
        }

        override suspend fun delete(dice: Dice) {
            list.removeAll { it.serialNumber.number == dice.serialNumber.number }
        }
    }

}
