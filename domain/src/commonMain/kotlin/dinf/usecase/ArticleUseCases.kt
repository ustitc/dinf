package dinf.usecase

import dinf.domain.Article
import dinf.types.ArticleID
import dinf.types.PInt

interface ArticleUseCases {

    fun articles(limit: PInt): List<Article>

    fun article(id: ArticleID): Article?

    class Stub(private val articles: List<Article>) : ArticleUseCases {

        override fun articles(limit: PInt): List<Article> {
            return articles
        }

        override fun article(id: ArticleID): Article? {
            return articles.find { it.id == id }
        }

    }

}
