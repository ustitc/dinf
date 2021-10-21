package dinf.data

import arrow.core.Either
import dinf.types.*
import kotlinx.datetime.Instant

interface ArticleRepository {

    fun findAll(limit: UInt): List<Article>

    fun findByID(id: ArticleID): Article?

    fun findAllByUserID(userID: UserID): List<Article>

    fun save(entity: ArticleSaveEntity): Article

    fun update(entity: ArticleEditEntity): Either<EntityNotFoundError, Article>

    fun deleteByID(articleID: ArticleID)

    fun deleteAllByIDIn(ids: List<ArticleID>)

}

data class ArticleSaveEntity(
    val name: NotBlankString,
    val description: String,
    val values: Values,
    val userID: UserID,
    val creationTime: Instant,
    val lastUpdateTime: Instant,
)

data class ArticleEditEntity(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val values: Values,
    val lastUpdateTime: Instant,
)
