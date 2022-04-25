package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface DiceRepository {

    fun flow(): Flow<Dice>

    suspend fun oneOrNull(hashID: HashID): Dice? {
        return oneOrNull(hashID.toID())
    }

    suspend fun oneOrNull(id: ID): Dice?

    suspend fun list(ids: List<ID>): List<Dice>

    suspend fun remove(dice: Dice)

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceRepository {

        override fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun oneOrNull(id: ID): Dice? {
            return list.firstOrNull { it.id.number == id.number }
        }

        override suspend fun list(ids: List<ID>): List<Dice> {
            return ids.mapNotNull { oneOrNull(it) }
        }

        override suspend fun remove(dice: Dice) {}
    }

}
