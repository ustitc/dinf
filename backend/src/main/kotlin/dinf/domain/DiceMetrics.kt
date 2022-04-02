package dinf.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface DiceMetrics {

    suspend fun forDice(dice: Dice): Metric

    fun popularSNs(): Flow<SN>

    suspend fun remove(metric: Metric)

    class InMemory private constructor(private val map: MutableMap<SN, Metric>) : DiceMetrics {

        constructor() : this(mutableMapOf())

        constructor(vararg initial: Pair<Dice, Metric>) : this(
            initial
                .toMap()
                .mapKeys { it.key.serialNumber }
                .toMutableMap()
        )

        override suspend fun forDice(dice: Dice): Metric {
            return map.getOrPut(dice.serialNumber) { InMemoryMetric() }
        }

        override fun popularSNs(): Flow<SN> {
            return map.entries
                .sortedByDescending { it.value.clicks }
                .map { it.key }
                .asFlow()
        }

        override suspend fun remove(metric: Metric) {
            val toDelete = map.filter { it.value == metric }.keys
            toDelete.forEach {
                map.remove(it)
            }
        }

        fun clear() {
            map.clear()
        }

        private class InMemoryMetric private constructor(simple: Metric.Simple) : Metric by simple {

            constructor() : this(Metric.Simple(0))

        }

    }

}
