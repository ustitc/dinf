package dinf.usecase

import dinf.domain.Article
import dinf.types.ArticleID
import dinf.types.PInt

interface ArticleUseCases {

    suspend fun articles(limit: PInt): List<Article>

    suspend fun article(id: ArticleID): Article?

    class Stub(private val articles: List<Article>) : ArticleUseCases {

        override suspend fun articles(limit: PInt): List<Article> {
            return articles
        }

        override suspend fun article(id: ArticleID): Article? {
            return articles.find { it.id == id }
        }

    }

}
