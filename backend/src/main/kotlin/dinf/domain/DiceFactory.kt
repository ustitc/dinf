package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface DiceFactory {

    suspend fun create(name: Name, edges: Edges): Dice

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceFactory {

        override suspend fun create(name: Name, edges: Edges): Dice {
            val dice = Dice.Stub()
            list.add(dice)
            return dice
        }
    }

    class Logging(
        private val inner: DiceFactory,
        private val logger: Logger = LoggerFactory.getLogger(Logging::class.java)
    ) : DiceFactory {

        override suspend fun create(name: Name, edges: Edges): Dice {
            val result = inner.create(name, edges)
            logger.info("Saved dice for id: ${result.id}")
            return result
        }
    }
}
