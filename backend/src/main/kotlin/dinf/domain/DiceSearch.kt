package dinf.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.time.delay
import java.time.Duration

fun interface DiceSearch : suspend (SearchQuery) -> List<Dice> {

    class Stub(private val block: () -> List<Dice>) : DiceSearch by DiceSearch({ block() }) {
        constructor(vararg dices: Dice) : this({ dices.toList() })
    }

    class Empty : DiceSearch by DiceSearch({ emptyList() })

    class Delay(private val main: DiceSearch, private val duration: Duration) : DiceSearch by DiceSearch({
        delay(duration)
        main.invoke(it)
    })

    class Simple(private val dices: Dices = Dices.Stub()) : DiceSearch by DiceSearch({ query ->
        dices.flow().filter { it.name.print().contains(query.text) }.toList()
    })

    class PopularFirst(private val search: DiceSearch, private val metrics: DiceMetrics) :
        DiceSearch by DiceSearch({ text ->
            search.invoke(text)
                .map { it to metrics.forDiceOrZero(it) }
                .sortedByDescending { it.second.clicks }
                .map { it.first }
        })

}
