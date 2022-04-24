package dinf.adapters

import com.meilisearch.sdk.Index
import com.meilisearch.sdk.SearchRequest
import dinf.domain.DiceSearch
import dinf.domain.DiceRepository
import dinf.domain.ID
import dinf.types.toPLongOrNull

class MeiliDiceSearch(private val index: Index, private val diceRepository: DiceRepository) : DiceSearch by DiceSearch({ query ->
    val searchRequest = SearchRequest(
        query.text,
        query.offset,
        query.limit
    )
    val serials = index.search(searchRequest)
        .hits
        .map { it[MeiliDiceCollection.idField]!! as Double }
        .map { it.toLong().toPLongOrNull()!! }
        .map { ID(it) }
    diceRepository.list(serials)
})
