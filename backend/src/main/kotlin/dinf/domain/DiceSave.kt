package dinf.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun interface DiceSave : suspend (Dice) -> Dice {

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceSave by DiceSave({
        list.add(it)
        it
    })

    class Logging(
        private val inner: DiceSave,
        private val logger: Logger = LoggerFactory.getLogger(Logging::class.java)
    ) : DiceSave by DiceSave({ dice ->
        val result = inner.invoke(dice)
        logger.info("Saved dice for id: ${result.id}")
        result
    })

    class Composite(
        private val list: List<DiceSave>
    ) : DiceSave by DiceSave({ dice ->
        var current = dice
        for (save in list) {
            current = save.invoke(current)
        }
        current
    }) {

        constructor(vararg saves: DiceSave) : this(saves.asList())

    }

}
