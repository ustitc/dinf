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

    override fun saveArticle(article: NewArticle): Article {
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

    override fun showOwnArticles(): List<Article> =
        repository.findAllByUserID(id)

    override fun editOwnArticle(article: EditedArticle): Either<ArticleError, Article> =
        hasEditPermission(article.id).flatMap { hasPermission ->
            if (hasPermission) {
                edit(article).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun deleteOwnArticle(id: ArticleID): Either<ArticleError, Unit> =
        hasEditPermission(id).flatMap { hasPermission ->
            if (hasPermission) {
                delete(id).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun deleteAllOwnArticles() {
        val articleIDs = repository.findAllByUserID(id).map { it.id }
        repository.deleteAllByIDIn(articleIDs)
    }

    private fun edit(article: EditedArticle): Article =
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

    private fun delete(id: ArticleID) = repository.deleteByID(id)

    private fun hasEditPermission(articleID: ArticleID): Either<ArticleNotFoundError, Boolean> {
        val exists = repository.findByID(articleID)?.let { it.author.id == this.id }
        return exists?.right() ?: ArticleNotFoundError.left()
    }

}
