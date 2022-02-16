package dinf.adapters

import com.meilisearch.sdk.Index
import dinf.domain.Dice
import dinf.domain.DiceSearch
import dinf.domain.Dices
import dinf.domain.SerialNumber

class MeiliDiceSearch(private val index: Index, private val dices: Dices) : DiceSearch {

    override suspend fun forText(text: String): List<Dice> {
        val serials = index.search(text)
            .hits
            .map { it[MeiliDiceCollection.idField]!! as Double }
            .map { it.toLong() }
            .map { SerialNumber.Simple(it) }
        return dices.list(serials)
    }
}
