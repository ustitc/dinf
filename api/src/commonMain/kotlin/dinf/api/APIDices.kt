package dinf.api

import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.ID
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
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
            client.get<List<APIDice>>(urlString = "$baseURL/dices")
        }!!.asFlow()
    }

    override suspend fun dice(id: ID): Dice? {
        TODO("Not yet implemented")
    }

    override suspend fun create(dice: Dice) {
        withTimeoutOrNull(timeout) {
            client.post<Any>("$baseURL/dices") {
                contentType(ContentType.Application.Json)
                body = APIDice(dice)
            }
        }
    }
}
