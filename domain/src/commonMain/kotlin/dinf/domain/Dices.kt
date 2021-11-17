package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Dices {

    suspend fun create(edges: Edges)

    suspend fun flow(): Flow<Dice>

    suspend fun dice(id: Int): Dice?

    class Stub(private val list: List<Dice>) : Dices {

        override suspend fun create(edges: Edges) {
            TODO("Not yet implemented")
        }

        override suspend fun flow(): Flow<Dice> {
            return list.asFlow()
        }

        override suspend fun dice(id: Int): Dice? {
            return list.getOrNull(id)
        }
    }

}
