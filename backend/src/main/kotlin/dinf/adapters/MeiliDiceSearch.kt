package dinf.adapters

import com.meilisearch.sdk.Index
import com.meilisearch.sdk.SearchRequest
import dinf.domain.DiceSearch
import dinf.domain.Dices
import dinf.domain.ID

class MeiliDiceSearch(private val index: Index, private val dices: Dices) : DiceSearch by DiceSearch({ query ->
    val searchRequest = SearchRequest(
        query.text,
        query.offset,
        query.limit
    )
    val serials = index.search(searchRequest)
        .hits
        .map { it[MeiliDiceCollection.idField]!! as Double }
        .map { it.toLong() }
        .map { ID(it) }
    dices.list(serials)
})
