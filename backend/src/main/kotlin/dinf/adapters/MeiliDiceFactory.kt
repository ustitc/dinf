package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.Dice
import dinf.domain.DiceFactory
import org.json.JSONObject

class MeiliDiceFactory(private val index: Index) : DiceFactory {

    override suspend fun create(dice: Dice): Dice {
        val json = JSONObject().also {
            it.append(MeiliDiceCollection.idField, dice.id.number)
            it.append(MeiliDiceCollection.nameField, dice.name.print())
        }
        index.addDocuments(json.toString())
        return dice
    }
}
