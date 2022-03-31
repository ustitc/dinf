package dinf

import com.meilisearch.sdk.Index
import dinf.adapters.MeiliDiceSave
import dinf.domain.Dices
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.time.delay
import java.time.Duration

class ReindexJob(
    private val dices: Dices,
    meiliDiceIndex: Index,
    private val delay: Duration
) {

    private val meiliDiceSave = MeiliDiceSave(meiliDiceIndex)

    fun asFlow() = flow {
        while (true) {
            delay(delay)
            dices
                .flow()
                .onEach { emit(it) }
                .collect()
        }
    }.onEach {
        meiliDiceSave.invoke(it)
    }

}
