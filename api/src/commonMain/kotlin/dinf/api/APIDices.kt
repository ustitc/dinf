package dinf.api

import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withTimeoutOrNull

class APIDices(
    private val baseURL: String,
    private val timeout: Long = 2000
) : Dices {

    private val client = HttpClient {
        install(JsonFeature)
    }

    override suspend fun flow(): Flow<Dice> {
        return withTimeoutOrNull(timeout) {
            client.get<List<APIDice>>(urlString = "$baseURL/dice/list")
        }!!.asFlow()
    }

    override suspend fun create(edges: Edges) {
        TODO("Not yet implemented")
    }
}
