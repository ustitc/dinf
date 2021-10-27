package dinf.usecase

import arrow.core.*
import dinf.data.ArticleEditEntity
import dinf.data.ArticleRepository
import dinf.data.ArticleSaveEntity
import dinf.domain.User
import dinf.types.*
import kotlinx.datetime.Clock

class ArticleUseCasesImpl(private val repository: ArticleRepository) : ArticleUseCases {

    override fun showManyArticles(limit: UInt): List<Article> =
        repository.findAll(limit)

    override fun showArticle(id: ArticleID): Either<ArticleNotFoundError, Article> =
        Either.fromNullable(
            repository.findByID(id)
        ).mapLeft { ArticleNotFoundError }

    override fun User.saveArticle(article: NewArticle): Article {
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

    override fun User.showOwnArticles(): List<Article> =
        repository.findAllByUserID(id)

    override fun User.editOwnArticle(article: EditedArticle): Either<ArticleError, Article> =
        hasEditPermission(article.id).flatMap { hasPermission ->
            if (hasPermission) {
                edit(article).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun User.deleteOwnArticle(id: ArticleID): Either<ArticleError, Unit> =
        hasEditPermission(id).flatMap { hasPermission ->
            if (hasPermission) {
                delete(id).right()
            } else {
                ArticleNoPermissionError.left()
            }
        }

    override fun User.deleteAllOwnArticles() {
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

    private fun User.hasEditPermission(articleID: ArticleID): Either<ArticleNotFoundError, Boolean> =
        showArticle(articleID).map { it.author.id == this.id }

}
