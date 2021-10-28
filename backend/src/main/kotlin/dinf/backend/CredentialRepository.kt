package dinf.backend

import dinf.types.Credential
import dinf.types.GithubCredential
import dinf.types.GoogleCredential
import dinf.types.UserID

interface CredentialRepository<T : Credential> {

    fun save(entity: CredentialEntity<T>)

    fun findUserByCredID(credID: T): UserEntity?

}

data class CredentialEntity<T : Credential>(
    val userID: UserID,
    val credID: T
)

class DelegatingCredentialRepository(
    private val githubCredRepo: CredentialRepository<GithubCredential>,
    private val googleCredRepo: CredentialRepository<GoogleCredential>,
) : CredentialRepository<Credential> {

    @Suppress("UNCHECKED_CAST")
    override fun save(entity: CredentialEntity<Credential>) = when (entity.credID) {
        is GoogleCredential -> googleCredRepo.save(entity as CredentialEntity<GoogleCredential>)
        is GithubCredential -> githubCredRepo.save(entity as CredentialEntity<GithubCredential>)
    }

    override fun findUserByCredID(credID: Credential): UserEntity? = when (credID) {
        is GoogleCredential -> googleCredRepo.findUserByCredID(credID)
        is GithubCredential -> githubCredRepo.findUserByCredID(credID)
    }

}
