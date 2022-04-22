package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.DiceSave
import org.json.simple.JSONObject

class MeiliDiceSave(private val index: Index) : DiceSave by DiceSave({ dice ->
    val json = JSONObject().also {
        it[MeiliDiceCollection.idField] = dice.id.number
        it[MeiliDiceCollection.nameField] = dice.name.print()
    }
    index.addDocuments(json.toString())
    dice
})
