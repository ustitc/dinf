package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Dices {

    suspend fun create(dice: Dice)

    suspend fun flow(): Flow<Dice>

    suspend fun dice(serialNumber: SerialNumber): Dice?

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : Dices {

        override suspend fun create(dice: Dice) {
            list.add(dice)
        }

        override suspend fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun dice(serialNumber: SerialNumber): Dice? {
            return list.firstOrNull { it.serialNumber.number == serialNumber.number }
        }
    }

}
