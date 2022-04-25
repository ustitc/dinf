package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.Dice
import dinf.domain.SearchIndexRepository
import org.json.JSONObject

class MeiliSearchIndexRepository(private val index: Index): SearchIndexRepository {

    override fun add(dice: Dice) {
        val json = JSONObject().also {
            it.append(MeiliDiceCollection.idField, dice.id.number)
            it.append(MeiliDiceCollection.nameField, dice.name.print())
        }
        index.addDocuments(json.toString())
    }
}
