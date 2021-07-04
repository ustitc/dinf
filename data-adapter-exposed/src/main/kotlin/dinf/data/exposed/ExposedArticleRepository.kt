package dinf.data.exposed

import arrow.core.Either
import dinf.data.ArticleEditEntity
import dinf.data.ArticleRepository
import dinf.data.ArticleSaveEntity
import dinf.data.EntityNotFoundError
import dinf.types.Article
import dinf.types.ArticleID
import dinf.types.UserID

class ExposedArticleRepository : ArticleRepository {

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

    override fun update(entity: ArticleEditEntity): Either<EntityNotFoundError, Article> {
        TODO("Not yet implemented")
    }

    override fun deleteByID(articleID: ArticleID) {
        TODO("Not yet implemented")
    }

    override fun deleteAllByIDIn(ids: List<ArticleID>) {
        TODO("Not yet implemented")
    }
}
