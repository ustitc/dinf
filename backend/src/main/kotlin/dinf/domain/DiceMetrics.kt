package dinf.domain

interface DiceMetrics {

    suspend fun forDice(dice: Dice): Metric

    class InMemory private constructor(private val map: MutableMap<Long, Metric>) : DiceMetrics {

        constructor() : this(mutableMapOf())

        constructor(vararg initial: Pair<Dice, Metric>) : this(
            initial
                .toMap()
                .mapKeys { it.key.serialNumber.number }
                .toMutableMap()
        )

        override suspend fun forDice(dice: Dice): Metric {
            return map.getOrPut(dice.serialNumber.number) { InMemoryMetric() }
        }

        private class InMemoryMetric private constructor(private var value: Clicks) : Metric {

            constructor() : this(0L)

            override suspend fun increment() {
                value += 1
            }

            override suspend fun clicks(): Clicks {
                return value
            }
        }

    }

}
