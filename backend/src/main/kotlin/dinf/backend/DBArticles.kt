package dinf.backend

import dinf.domain.Article
import dinf.domain.Articles
import dinf.exposed.ArticleEntity
import dinf.types.ArticleID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBArticles : Articles {

    override suspend fun flow(): Flow<Article> = newSuspendedTransaction {
        ArticleEntity
            .all()
            .map { it.toArticle() }
            .asFlow()
    }

    override suspend fun article(id: ArticleID): Article? = newSuspendedTransaction {
        ArticleEntity
            .findById(id.toInt())
            ?.toArticle()
    }

}
