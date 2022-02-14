package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.Dice
import dinf.domain.DiceSave
import org.json.simple.JSONObject

class MeiliDiceSave(private val index: Index) : DiceSave {

    override suspend fun create(dice: Dice): Dice {
        val json = JSONObject().also {
            it[MeiliDiceCollection.idField] = dice.serialNumber.number
            it[MeiliDiceCollection.nameField] = dice.name.nbString.toString()
        }
        index.addDocuments(json.toString())
        return dice
    }
}
