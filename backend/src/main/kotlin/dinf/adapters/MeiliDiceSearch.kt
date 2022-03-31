package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.DiceSearch
import dinf.domain.Dices
import dinf.domain.SerialNumber

class MeiliDiceSearch(private val index: Index, private val dices: Dices) : DiceSearch by DiceSearch({ text ->
    val serials = index.search(text)
        .hits
        .map { it[MeiliDiceCollection.idField]!! as Double }
        .map { it.toLong() }
        .map { SerialNumber.Simple(it) }
    dices.list(serials)
})
