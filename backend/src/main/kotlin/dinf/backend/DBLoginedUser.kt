package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.domain.Author
import dinf.domain.LoginedUser
import dinf.types.UserID
import dinf.types.UserName
import org.jetbrains.exposed.sql.transactions.transaction

class DBLoginedUser(
    private val userID: UserID,
    articleRepository: ArticleRepository
) : LoginedUser {

    private val author: Author = DBAuthor(userID, articleRepository)

    override fun change(name: UserName) = transaction {
        val intID = userID.toInt()
        val user = UserEntity.findById(intID)
        if (user == null) {
            throw IllegalStateException("Can't update user with id=$intID. Entry doesn't exist")
        } else {
            user.name = name.toString()
        }
    }

    override fun deleteAccount() = transaction<Unit> {
        author.deleteArticles()
        UserEntity.findById(userID.toInt())?.delete()
    }

    override fun toAuthor(): Author {
        return author
    }
}
