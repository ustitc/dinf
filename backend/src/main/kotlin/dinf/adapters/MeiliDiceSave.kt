package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.DiceSave
import org.json.JSONObject

class MeiliDiceSave(private val index: Index) : DiceSave by DiceSave({ dice ->
    val json = JSONObject().also {
        it.append(MeiliDiceCollection.idField, dice.id.number)
        it.append(MeiliDiceCollection.nameField, dice.name.print())
    }
    index.addDocuments(json.toString())
    dice
})
