package dinf.backend

import dinf.domain.Article
import dinf.exposed.ArticleEntity
import dinf.types.ArticleID
import dinf.types.PInt
import dinf.usecase.ArticleUseCases
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBArticleUseCases : ArticleUseCases {

    override suspend fun articles(limit: PInt): List<Article> = newSuspendedTransaction {
        ArticleEntity
            .all()
            .limit(limit.value)
            .map { it.toArticle() }
    }

    override suspend fun article(id: ArticleID): Article? = newSuspendedTransaction {
        ArticleEntity
            .findById(id.toInt())
            ?.toArticle()
    }

}
