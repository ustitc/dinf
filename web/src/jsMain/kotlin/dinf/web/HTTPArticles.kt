package dinf.web

import dinf.api.ArticleDTO
import dinf.domain.Article
import dinf.domain.Articles
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.Values
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withTimeoutOrNull

class HTTPArticles(
    private val baseURL: String,
    private val timeout: Long = 2000
) : Articles {

    private val client = HttpClient {
        install(JsonFeature)
    }

    override suspend fun flow(): Flow<Article> {
        return withTimeoutOrNull(timeout) {
            client.get<List<ArticleDTO>>(urlString = "$baseURL/article/list")
        }?.map {
            Article(
                id = ArticleID(it.id),
                content = Content(
                    title = NBString(it.title),
                    description = it.description,
                    values = Values("red", "green", "blue", "purple", "cyan", "yellow")
                ),
                author = Author.Stub()
            )
        }!!.asFlow()
    }

    override suspend fun article(id: ArticleID): Article? {
        TODO("Not yet implemented")
    }
}
