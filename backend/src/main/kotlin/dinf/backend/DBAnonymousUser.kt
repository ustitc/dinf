package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.data.exposed.UserTable
import dinf.domain.AnonymousUser
import dinf.domain.LoginedUser
import dinf.types.Credential
import dinf.types.GithubCredential
import dinf.types.GoogleCredential
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class DBAnonymousUser : AnonymousUser {

    override fun toLogined(credential: Credential): LoginedUser = transaction {
        findByCredential(credential) ?: register(credential)
    }

    private fun findByCredential(credential: Credential): LoginedUser? {
        return when (credential) {
            is GithubCredential -> {
                UserEntity.find { UserTable.githubCredential eq credential.toInt() }
            }
            is GoogleCredential -> {
                UserEntity.find { UserTable.googleCredential eq credential.toString() }
            }
        }.singleOrNull()?.let { DBLoginedUser(it) }
    }

    private fun register(credential: Credential): DBLoginedUser {
        val entity = UserEntity.new {
            name = "test"
            registrationTime = Instant.now()
        }
        when (credential) {
            is GithubCredential -> {
                entity.githubCredential = credential.toInt()
            }
            is GoogleCredential -> {
                entity.googleCredential = credential.toString()
            }
        }
        return DBLoginedUser(entity)
    }

}
