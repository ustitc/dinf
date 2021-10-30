package dinf.backend

import dinf.domain.Article
import dinf.exposed.ArticleEntity
import dinf.types.*
import dinf.usecase.ArticleUseCases
import org.jetbrains.exposed.sql.transactions.transaction

class DBArticleUseCases : ArticleUseCases {

    override fun articles(limit: PInt): List<Article> = transaction {
        ArticleEntity
            .all()
            .limit(limit.value)
            .map { it.toArticle() }
    }

    override fun article(id: ArticleID): Article? = transaction {
        ArticleEntity
            .findById(id.toInt())
            ?.toArticle()
    }

}
