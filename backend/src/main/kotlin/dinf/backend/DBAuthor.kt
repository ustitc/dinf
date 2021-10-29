package dinf.backend

import dinf.exposed.ArticleEntity
import dinf.exposed.ArticleTable
import dinf.exposed.UserEntity
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.Article
import dinf.types.ArticleID
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class DBAuthor(
    private val userEntity: UserEntity
) : Author {

    private val authorID = userEntity.id.value

    override fun createArticle(content: Content): Article = transaction {
        val entity = ArticleEntity.new {
            name = content.title.toString()
            description = content.description
            values = content.values.list.map { it.toString() }.toTypedArray()
            author = userEntity
            creation = Instant.now()
            lastUpdate = Instant.now()
        }
        entity.toArticle()
    }

    override fun articles(): List<Article> = transaction {
        ArticleEntity
            .find { ArticleTable.authorID eq authorID }
            .map { it.toArticle() }
    }

    override fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit> {
        val entity = findAuthorArticle(articleID)
        return if (entity != null) {
            val content = entity.toContent()
            block.invoke(content)
            entity.name = content.title.toString()
            entity.description = content.description
            entity.values = content.values.list.map { it.toString() }.toTypedArray()
            Result.success(Unit)
        } else {
            Result.failure(
                ArticleNotFoundException(
                    userEntity = userEntity,
                    articleID = articleID
                )
            )
        }
    }

    override fun deleteArticle(articleID: ArticleID): Result<Unit> = transaction {
        val entity = findAuthorArticle(articleID)
        if (entity != null) {
            entity.delete()
            Result.success(Unit)
        } else {
            Result.failure(
                ArticleNotFoundException(userEntity = userEntity, articleID = articleID)
            )
        }
    }

    override fun deleteArticles() = transaction<Unit> {
        ArticleTable.deleteWhere { ArticleTable.authorID eq authorID }
    }

    private fun findAuthorArticle(articleID: ArticleID): ArticleEntity? {
        return ArticleEntity
            .findById(articleID.toInt())
            ?.takeIf { it.author.id.value == authorID }
    }
}
