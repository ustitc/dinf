package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Dices {

    fun flow(): Flow<Dice>

    suspend fun oneOrNull(serialNumber: SerialNumber): Dice?

    suspend fun list(serials: List<SerialNumber>): List<Dice>

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : Dices {

        override fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun oneOrNull(serialNumber: SerialNumber): Dice? {
            return list.firstOrNull { it.serialNumber.number == serialNumber.number }
        }

        override suspend fun list(serials: List<SerialNumber>): List<Dice> {
            return serials.mapNotNull { oneOrNull(it) }
        }
    }

}
