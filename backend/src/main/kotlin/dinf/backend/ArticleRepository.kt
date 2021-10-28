package dinf.backend

import dinf.types.*

interface ArticleRepository {

    fun findAll(limit: UInt): List<Article>

    fun findByID(id: ArticleID): Article?

    fun findAllByUserID(userID: UserID): List<Article>

    fun save(entity: ArticleSaveEntity): Article

    fun update(entity: ArticleEditEntity): Result<Article>

    fun deleteByID(articleID: ArticleID)

    fun deleteAllByIDIn(ids: List<ArticleID>)

    fun deleteAllByUserID(id: UserID)

    class Stub : ArticleRepository {
        override fun findAll(limit: UInt): List<Article> {
            TODO("Not yet implemented")
        }

        override fun findByID(id: ArticleID): Article? {
            TODO("Not yet implemented")
        }

        override fun findAllByUserID(userID: UserID): List<Article> {
            TODO("Not yet implemented")
        }

        override fun save(entity: ArticleSaveEntity): Article {
            TODO("Not yet implemented")
        }

        override fun update(entity: ArticleEditEntity): Result<Article> {
            TODO("Not yet implemented")
        }

        override fun deleteByID(articleID: ArticleID) {
            TODO("Not yet implemented")
        }

        override fun deleteAllByIDIn(ids: List<ArticleID>) {
            TODO("Not yet implemented")
        }

        override fun deleteAllByUserID(id: UserID) {
            TODO("Not yet implemented")
        }
    }

}

data class ArticleSaveEntity(
    val name: NotBlankString,
    val description: String,
    val values: Values,
    val userID: UserID
)

data class ArticleEditEntity(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val values: Values
)
