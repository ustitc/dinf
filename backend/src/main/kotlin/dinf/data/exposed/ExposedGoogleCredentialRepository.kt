package dinf.data.exposed

import dinf.backend.CredentialEntity
import dinf.types.GoogleCredential
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert

class ExposedGoogleCredentialRepository : AbstractCredentialRepository<GoogleCredential>(GoogleCredentialTable) {

    override val findUserByCredIDCondition: (GoogleCredential) -> Op<Boolean>
        get() = { GoogleCredentialTable.googleID eq it.toString() }

    override fun save(entity: CredentialEntity<GoogleCredential>) {
        GoogleCredentialTable.insert {
            it[userID] = entity.userID.toInt()
            it[googleID] = entity.credID.toString()
        }
    }

}
