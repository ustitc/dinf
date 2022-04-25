package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface DiceMetricRepository {

    suspend fun forID(id: ID): Metric?

    suspend fun forIDOrZero(id: ID): Metric {
        return forID(id) ?: Metric.zero()
    }

    fun popularIDs(): Flow<ID>

    suspend fun removeForID(id: ID)

    suspend fun create(id: ID, metric: Metric)

    class InMemory private constructor(private val map: MutableMap<ID, Metric>) : DiceMetricRepository {

        constructor() : this(mutableMapOf())

        constructor(vararg initial: Pair<Dice, Metric>) : this(
            initial
                .toMap()
                .mapKeys { it.key.id }
                .toMutableMap()
        )

        override suspend fun forID(id: ID): Metric? {
            return map[id]
        }

        override fun popularIDs(): Flow<ID> {
            return map.entries
                .sortedByDescending { it.value.clicks }
                .map { it.key }
                .asFlow()
        }

        override suspend fun removeForID(id: ID) {
            map.remove(id)
        }

        override suspend fun create(id: ID, metric: Metric) {
            map[id] = metric
        }

        fun clear() {
            map.clear()
        }

    }

}
