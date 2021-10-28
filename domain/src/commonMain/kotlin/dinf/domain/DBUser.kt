package dinf.domain

import arrow.core.getOrHandle
import dinf.data.ArticleRepository
import dinf.data.UserEditEntity
import dinf.data.UserRepository
import dinf.types.UserID
import dinf.types.UserName

class DBUser(
    private val id: UserID,
    private val userRepository: UserRepository,
    articleRepository: ArticleRepository
) : LoginedUser {

    private val author: Author = DBAuthor(id, articleRepository)

    override fun change(name: UserName) {
        userRepository
            .update(UserEditEntity(id = id, name = name))
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }
    }

    override fun deleteAccount() {
        author.deleteArticles()
        userRepository.deleteByUserID(id)
    }

    override fun toAuthor(): Author {
        return author
    }
}
