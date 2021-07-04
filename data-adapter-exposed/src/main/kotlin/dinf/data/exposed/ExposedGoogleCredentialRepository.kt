package dinf.data.exposed

import dinf.data.CredentialEntity
import dinf.types.GoogleCredential
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert

class ExposedGoogleCredentialRepository : AbstractCredentialRepository<GoogleCredential>(GoogleCredentials) {

    override val findUserByCredIDCondition: (GoogleCredential) -> Op<Boolean>
        get() = { GoogleCredentials.googleID eq it.toString() }

    override fun save(entity: CredentialEntity<GoogleCredential>) {
        GoogleCredentials.insert {
            it[userID] = entity.userID.toInt()
            it[googleID] = entity.credID.toString()
        }
    }

}
