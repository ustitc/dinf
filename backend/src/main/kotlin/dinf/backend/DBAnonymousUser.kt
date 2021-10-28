package dinf.backend

import dinf.data.exposed.GithubCredentialTable
import dinf.data.exposed.GoogleCredentialTable
import dinf.data.exposed.UserEntity
import dinf.data.exposed.UserTable
import dinf.domain.AnonymousUser
import dinf.domain.LoginedUser
import dinf.types.Credential
import dinf.types.GithubCredential
import dinf.types.GoogleCredential
import dinf.types.UserID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class DBAnonymousUser(
    private val articleRepository: ArticleRepository
) : AnonymousUser {

    override fun toLogined(credential: Credential): LoginedUser = transaction {
        findByCredential(credential) ?: register(credential)
    }

    private fun findByCredential(credential: Credential): LoginedUser? {
        val row = when (credential) {
            is GithubCredential -> {
                (GithubCredentialTable innerJoin UserTable)
                    .select { GithubCredentialTable.githubID eq credential.toInt() }
            }
            is GoogleCredential -> {
                (GoogleCredentialTable innerJoin UserTable)
                    .select { GoogleCredentialTable.googleID eq credential.toString() }
            }
        }
        return row
            .map {
                DBLoginedUser(
                    userID = UserID.orNull(it[UserTable.id].value)!!,
                    articleRepository = articleRepository
                )
            }.firstOrNull()
    }

    private fun register(credential: Credential): DBLoginedUser {
        val user = UserEntity.new {
            name = "test"
            registrationTime = Instant.now()
        }
        when (credential) {
            is GithubCredential -> {
                GithubCredentialTable.insert {
                    it[userID] = user.id.value
                    it[githubID] = credential.toInt()
                }
            }
            is GoogleCredential -> {
                GoogleCredentialTable.insert {
                    it[userID] = user.id.value
                    it[googleID] = credential.toString()
                }
            }
        }
        return DBLoginedUser(
            userID = UserID.orNull(user.id.value)!!,
            articleRepository = articleRepository
        )
    }

}
