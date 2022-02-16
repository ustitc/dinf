package dinf.domain

interface DiceMetrics {

    suspend fun increment(dice: Dice)

    class Simple(private val map: MutableMap<SerialNumber, Long> = mutableMapOf()): DiceMetrics {
        override suspend fun increment(dice: Dice) {
            map.putIfAbsent(dice.serialNumber, 0L)
            map.computeIfPresent(dice.serialNumber) { _, i -> i + 1 }
        }
    }

}
