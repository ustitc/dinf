package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Dices {

    fun flow(): Flow<Dice>

    suspend fun diceOrNull(serialNumber: SerialNumber): Dice?

    suspend fun dices(serials: List<SerialNumber>): List<Dice>

    suspend fun delete(dice: Dice)

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : Dices {

        override fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun diceOrNull(serialNumber: SerialNumber): Dice? {
            return list.firstOrNull { it.serialNumber.number == serialNumber.number }
        }

        override suspend fun dices(serials: List<SerialNumber>): List<Dice> {
            return serials.mapNotNull { diceOrNull(it) }
        }

        override suspend fun delete(dice: Dice) {
            list.removeAll { it.serialNumber.number == dice.serialNumber.number }
        }
    }

}
