package dinf.domain

import kotlinx.coroutines.flow.Flow

interface Dices {

    suspend fun create(edges: List<Edge>)

    suspend fun flow(): Flow<Dice>

    suspend fun dice(id: Int): Dice?

}
