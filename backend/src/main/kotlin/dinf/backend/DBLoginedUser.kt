package dinf.backend

import dinf.domain.Author
import dinf.domain.LoginedUser
import dinf.exposed.UserEntity
import dinf.types.UserName
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DBLoginedUser(
    private val userEntity: UserEntity
) : LoginedUser {

    private val author: Author = DBAuthor(userEntity)

    override suspend fun change(name: UserName) = newSuspendedTransaction {
        userEntity.name = name.toString()
    }

    override suspend fun deleteAccount() = newSuspendedTransaction {
        author.deleteArticles()
        userEntity.delete()
    }

    override suspend fun toAuthor(): Author {
        return author
    }
}
