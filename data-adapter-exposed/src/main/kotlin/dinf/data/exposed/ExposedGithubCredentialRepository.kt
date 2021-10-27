package dinf.data.exposed

import dinf.data.CredentialEntity
import dinf.types.GithubCredential
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedGithubCredentialRepository : AbstractCredentialRepository<GithubCredential>(GithubCredentialTable) {

    override val findUserByCredIDCondition: (GithubCredential) -> Op<Boolean>
        get() = { GithubCredentialTable.githubID eq it.toInt() }

    override fun save(entity: CredentialEntity<GithubCredential>): Unit = transaction {
        GithubCredentialTable.insert {
            it[userID] = entity.userID.toInt()
            it[githubID] = entity.credID.toInt()
        }
    }

}
