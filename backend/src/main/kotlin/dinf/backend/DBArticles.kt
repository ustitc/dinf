package dinf.backend

import dinf.domain.Article
import dinf.exposed.ArticleEntity
import dinf.types.ArticleID
import dinf.types.PInt
import dinf.domain.Articles
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBArticles : Articles {

    override suspend fun list(limit: PInt): List<Article> = newSuspendedTransaction {
        ArticleEntity
            .all()
            .limit(limit.toInt())
            .map { it.toArticle() }
    }

    override suspend fun article(id: ArticleID): Article? = newSuspendedTransaction {
        ArticleEntity
            .findById(id.toInt())
            ?.toArticle()
    }

}
