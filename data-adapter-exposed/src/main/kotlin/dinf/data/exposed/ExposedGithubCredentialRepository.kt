package dinf.data.exposed

import dinf.data.CredentialEntity
import dinf.types.GithubCredential
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedGithubCredentialRepository : AbstractCredentialRepository<GithubCredential>(GithubCredentials) {

    override val findUserByCredIDCondition: (GithubCredential) -> Op<Boolean>
        get() = { GithubCredentials.githubID eq it.toInt() }

    override fun save(entity: CredentialEntity<GithubCredential>): Unit = transaction {
        GithubCredentials.insert {
            it[userID] = entity.userID.toInt()
            it[githubID] = entity.credID.toInt()
        }
    }

}
