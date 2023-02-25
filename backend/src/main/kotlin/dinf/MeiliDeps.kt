package dinf

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config
import com.meilisearch.sdk.Index
import dinf.config.SearchConfig

class MeiliDeps(private val searchConfig: SearchConfig) {

    private val indexName = "dices"

    fun meiliDiceIndex(): Index {
        return meiliClient().index(indexName)
    }

    private fun meiliClient(): Client {
        return Client(
            Config(searchConfig.url, searchConfig.key)
        )
    }

}
