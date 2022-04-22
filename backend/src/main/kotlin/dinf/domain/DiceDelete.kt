package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun interface DiceDelete : suspend (Dice) -> Unit {

    class Stub : DiceDelete by DiceDelete({})

    class Logging(
        private val inner: DiceDelete,
        private val logger: Logger = LoggerFactory.getLogger(Logging::class.java)
    ) : DiceDelete by DiceDelete({ dice ->
        inner.invoke(dice)
        logger.info("Deleted dice for SN: ${dice.id}")
    })

    class Composite(private val list: List<DiceDelete>) : DiceDelete by DiceDelete({ dice ->
        list.forEach { delete ->
            delete.invoke(dice)
        }
    }) {

        constructor(vararg deletes: DiceDelete) : this(deletes.asList())

    }

}
