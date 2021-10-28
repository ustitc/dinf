package dinf.backend

import dinf.domain.Author
import dinf.domain.LoginedUser
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
            .getOrThrow()
    }

    override fun deleteAccount() {
        author.deleteArticles()
        userRepository.deleteByUserID(id)
    }

    override fun toAuthor(): Author {
        return author
    }
}
