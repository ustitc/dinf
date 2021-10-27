package dinf.domain

import arrow.core.*
import dinf.data.ArticleEditEntity
import dinf.data.ArticleRepository
import dinf.data.ArticleSaveEntity
import dinf.types.*
import kotlinx.datetime.Clock

class DBAuthor(
    override val id: UserID,
    private val repository: ArticleRepository
) : Author {

    override fun create(article: NewArticle): Article {
        val now = Clock.System.now()
        return repository.save(
            entity = ArticleSaveEntity(
                userID = id,
                name = article.name,
                description = article.description,
                values = article.values,
                creationTime = now,
                lastUpdateTime = now
            )
        )
    }

    override fun articles(): List<Article> =
        repository.findAllByUserID(id)

    override fun edit(article: EditedArticle): Either<ArticleError, Article> =
        hasEditPermission(article.id).flatMap { hasPermission ->
            if (hasPermission) {
                editArticle(article).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun delete(id: ArticleID): Either<ArticleError, Unit> =
        hasEditPermission(id).flatMap { hasPermission ->
            if (hasPermission) {
                repository.deleteByID(id).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun deleteArticles() {
        val articleIDs = repository.findAllByUserID(id).map { it.id }
        repository.deleteAllByIDIn(articleIDs)
    }

    private fun editArticle(article: EditedArticle): Article =
        repository
            .update(
                ArticleEditEntity(
                    id = article.id,
                    name = article.name,
                    description = article.description,
                    values = article.values,
                    lastUpdateTime = Clock.System.now()
                )
            )
            .getOrHandle { throw IllegalStateException("Found no article for id=${article.id}") }

    private fun hasEditPermission(articleID: ArticleID): Either<ArticleNotFoundError, Boolean> {
        val exists = repository.findByID(articleID)?.let { it.author.id == this.id }
        return exists?.right() ?: ArticleNotFoundError.left()
    }

}
