package dinf.data.exposed

import dinf.data.CredentialEntity
import dinf.data.CredentialRepository
import dinf.data.UserEntity
import dinf.types.GoogleCredential

class ExposedGoogleCredentialRepository : CredentialRepository<GoogleCredential> {

    override fun save(entity: CredentialEntity<GoogleCredential>) {
        TODO("Not yet implemented")
    }

    override fun findUserByCredID(credID: GoogleCredential): UserEntity? {
        TODO("Not yet implemented")
    }
}
