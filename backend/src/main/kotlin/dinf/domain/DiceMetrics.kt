package dinf.domain

interface DiceMetrics {

    suspend fun increment(dice: Dice)

    suspend fun clicks(dice: Dice): Long

    class Simple(private val map: MutableMap<SerialNumber, Long> = mutableMapOf()): DiceMetrics {

        override suspend fun increment(dice: Dice) {
            map.putIfAbsent(dice.serialNumber, 0L)
            map.computeIfPresent(dice.serialNumber) { _, i -> i + 1 }
        }

        override suspend fun clicks(dice: Dice): Long {
            return map.getOrDefault(dice.serialNumber, 0L)
        }
    }

}
