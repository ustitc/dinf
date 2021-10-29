package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.domain.Author
import dinf.domain.LoginedUser
import dinf.types.UserName
import org.jetbrains.exposed.sql.transactions.transaction

class DBLoginedUser(
    private val userEntity: UserEntity
) : LoginedUser {

    private val author: Author = DBAuthor(userEntity)

    override fun change(name: UserName) = transaction {
        userEntity.name = name.toString()
    }

    override fun deleteAccount() = transaction {
        author.deleteArticles()
        userEntity.delete()
    }

    override fun toAuthor(): Author {
        return author
    }
}
