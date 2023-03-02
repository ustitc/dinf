package dinf.domain

import kotlinx.coroutines.flow.Flow

interface DiceRepository {

    fun flow(): Flow<Dice>

    fun oneOrNull(id: ID): Dice?

    fun list(ids: List<ID>): List<Dice>

    fun remove(dice: Dice)

    fun update(dice: Dice)

    fun search(text: String): List<Dice>

}
