package dinf.data.exposed

import dinf.data.CredentialEntity
import dinf.data.CredentialRepository
import dinf.data.UserEntity
import dinf.types.GithubCredential

class ExposedGithubCredentialRepository : CredentialRepository<GithubCredential> {

    override fun save(entity: CredentialEntity<GithubCredential>) {
        TODO("Not yet implemented")
    }

    override fun findUserByCredID(credID: GithubCredential): UserEntity? {
        TODO("Not yet implemented")
    }
}
