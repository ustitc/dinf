package dinf.domain

import kotlinx.coroutines.flow.Flow

interface DiceRepository {

    fun flow(): Flow<Dice>

    suspend fun oneOrNull(publicID: PublicID): Dice? {
        return oneOrNull(publicID.toID())
    }

    suspend fun oneOrNull(id: ID): Dice?

    suspend fun list(ids: List<ID>): List<Dice>

    suspend fun remove(dice: Dice)

    suspend fun update(dice: Dice)

    fun search(text: String): List<Dice>

}
