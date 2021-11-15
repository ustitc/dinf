package dinf.domain

import dinf.types.ArticleID
import dinf.types.PInt

interface Articles {

    suspend fun list(limit: PInt): List<Article>

    suspend fun article(id: ArticleID): Article?

    class Stub(private val articles: List<Article>) : Articles {

        override suspend fun list(limit: PInt): List<Article> {
            return articles
        }

        override suspend fun article(id: ArticleID): Article? {
            return articles.find { it.id == id }
        }

    }

}
