package dinf

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config
import com.meilisearch.sdk.Index
import dinf.config.Search

class MeiliDeps(private val search: Search) {

    private val indexName = "dices"

    fun meiliDiceIndex(): Index {
        return meiliClient().index(indexName)
    }

    private fun meiliClient(): Client {
        return Client(
            Config(search.url, search.key)
        )
    }

}
