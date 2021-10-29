package dinf.backend

import dinf.data.exposed.ArticleEntity
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

    override fun article(id: ArticleID): Result<Article> {
        val entity = ArticleEntity.findById(id.toInt())
        return if (entity != null) {
            Result.success(entity.toArticle())
        } else {
            Result.failure(
                ArticleNotFoundException(id)
            )
        }
    }

}
