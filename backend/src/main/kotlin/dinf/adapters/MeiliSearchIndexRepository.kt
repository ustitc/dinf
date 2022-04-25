package dinf.adapters

import com.meilisearch.sdk.Index
import com.meilisearch.sdk.SearchRequest
import dinf.domain.Dice
import dinf.domain.ID
import dinf.domain.SearchIndexRepository
import dinf.domain.SearchQuery
import dinf.types.toPLongOrNull
import org.json.JSONObject

class MeiliSearchIndexRepository(
    private val index: Index,
) : SearchIndexRepository {

    override fun add(dice: Dice) {
        val json = JSONObject().also {
            it.append(MeiliDiceCollection.idField, dice.id.number)
            it.append(MeiliDiceCollection.nameField, dice.name.print())
        }
        index.addDocuments(json.toString())
    }

    override fun search(query: SearchQuery): List<ID> {
        val searchRequest = SearchRequest(
            query.text,
            query.offset,
            query.limit
        )
        return index.search(searchRequest)
            .hits
            .map { it[MeiliDiceCollection.idField]!! as Double }
            .map { it.toLong().toPLongOrNull()!! }
            .map { ID(it) }
    }
}
