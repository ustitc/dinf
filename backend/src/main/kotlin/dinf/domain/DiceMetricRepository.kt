package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface DiceMetricRepository {

    suspend fun forDice(dice: Dice): Metric?

    suspend fun forDiceOrZero(dice: Dice): Metric {
        return forDice(dice) ?: Metric.zero()
    }

    fun popularIDs(): Flow<ID>

    suspend fun removeForDice(dice: Dice)

    suspend fun create(dice: Dice, metric: Metric)

    class InMemory private constructor(private val map: MutableMap<ID, Metric>) : DiceMetricRepository {

        constructor() : this(mutableMapOf())

        constructor(vararg initial: Pair<Dice, Metric>) : this(
            initial
                .toMap()
                .mapKeys { it.key.id }
                .toMutableMap()
        )

        override suspend fun forDice(dice: Dice): Metric? {
            return map[dice.id]
        }

        override fun popularIDs(): Flow<ID> {
            return map.entries
                .sortedByDescending { it.value.clicks }
                .map { it.key }
                .asFlow()
        }

        override suspend fun removeForDice(dice: Dice) {
            map.remove(dice.id)
        }

        override suspend fun create(dice: Dice, metric: Metric) {
            map[dice.id] = metric
        }

        fun clear() {
            map.clear()
        }

    }

}
