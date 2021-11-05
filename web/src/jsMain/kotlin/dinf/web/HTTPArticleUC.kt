package dinf.web

import dinf.api.ArticleDTO
import dinf.domain.Article
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.PInt
import dinf.types.Values
import dinf.usecase.ArticleUseCases
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.withTimeoutOrNull

class HTTPArticleUC(
    private val baseURL: String,
    private val timeout: Long = 2000
) : ArticleUseCases {

    private val client = HttpClient {
        install(JsonFeature)
    }

    override suspend fun articles(limit: PInt): List<Article> {
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
        }!!
    }

    override suspend fun article(id: ArticleID): Article? {
        TODO("Not yet implemented")
    }
}
