package dinf.domain

import dinf.data.ArticleEditEntity
import dinf.data.ArticleRepository
import dinf.data.ArticleSaveEntity
import dinf.types.Article
import dinf.types.ArticleID
import dinf.types.UserID
import kotlinx.datetime.Clock

class DBAuthor(
    override val id: UserID,
    private val repository: ArticleRepository
) : Author {

    override fun createArticle(content: Content): Article {
        val now = Clock.System.now()
        return repository.save(
            entity = ArticleSaveEntity(
                userID = id,
                name = content.title,
                description = content.description,
                values = content.values,
                creationTime = now,
                lastUpdateTime = now
            )
        )
    }

    override fun articles(): List<Article> {
        return repository.findAllByUserID(id)
    }

    override fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit> {
        val article = findAuthorArticle(articleID)
        return if (article != null) {
            val content = Content(
                title = article.name,
                description = article.description,
                values = article.values
            )
            block.invoke(content)
            editArticle(articleID, content)
        } else {
            Result.failure(
                ArticleNotFoundException(authorID = id, articleID = articleID)
            )
        }
    }

    override fun deleteArticle(articleID: ArticleID): Result<Unit> {
        return if (hasEditPermission(articleID)) {
            repository.deleteByID(articleID)
            Result.success(Unit)
        } else {
            Result.failure(
                ArticleNotFoundException(authorID = id, articleID = articleID)
            )
        }
    }

    override fun deleteArticles() {
        val articleIDs = repository.findAllByUserID(id).map { it.id }
        repository.deleteAllByIDIn(articleIDs)
    }

    private fun editArticle(articleID: ArticleID, content: Content): Result<Unit> {
        return repository.update(
            ArticleEditEntity(
                id = articleID,
                name = content.title,
                description = content.description,
                values = content.values,
                lastUpdateTime = Clock.System.now()
            )
        ).map {  }
    }

    private fun findAuthorArticle(articleID: ArticleID): Article? {
        return repository
            .findByID(articleID)
            ?.takeIf { it.author.id == id }
    }

    private fun hasEditPermission(articleID: ArticleID): Boolean {
        return findAuthorArticle(articleID) != null
    }

}
