package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface DiceMetrics {

    suspend fun forDice(dice: Dice): Metric?

    suspend fun forDiceOrZero(dice: Dice): Metric {
        return forDice(dice) ?: Metric.zero()
    }

    fun popularSNs(): Flow<SN>

    suspend fun removeForDice(dice: Dice)

    suspend fun create(dice: Dice, metric: Metric)

    class InMemory private constructor(private val map: MutableMap<SN, Metric>) : DiceMetrics {

        constructor() : this(mutableMapOf())

        constructor(vararg initial: Pair<Dice, Metric>) : this(
            initial
                .toMap()
                .mapKeys { it.key.serialNumber }
                .toMutableMap()
        )

        override suspend fun forDice(dice: Dice): Metric? {
            return map[dice.serialNumber]
        }

        override fun popularSNs(): Flow<SN> {
            return map.entries
                .sortedByDescending { it.value.clicks }
                .map { it.key }
                .asFlow()
        }

        override suspend fun removeForDice(dice: Dice) {
            map.remove(dice.serialNumber)
        }

        override suspend fun create(dice: Dice, metric: Metric) {
            map[dice.serialNumber] = metric
        }

        fun clear() {
            map.clear()
        }

    }

}
