package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceFactory {

    suspend fun create(dice: Dice): Dice

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceFactory {

        override suspend fun create(dice: Dice): Dice {
            list.add(dice)
            return dice
        }
    }

    class Logging(
        private val inner: DiceFactory,
        private val logger: Logger = LoggerFactory.getLogger(Logging::class.java)
    ) : DiceFactory {

        override suspend fun create(dice: Dice): Dice {
            val result = inner.create(dice)
            logger.info("Saved dice for id: ${result.id}")
            return result
        }
    }

    class Composite(
        private val list: List<DiceFactory>
    ) : DiceFactory {

        constructor(vararg saves: DiceFactory) : this(saves.asList())

        override suspend fun create(dice: Dice): Dice {
            var current = dice
            for (factory in list) {
                current = factory.create(current)
            }
            return current
        }
    }
}
