package dinf.domain

import arrow.core.getOrHandle
import dinf.data.*
import dinf.types.*
import kotlinx.datetime.Clock

class DBUser(
    override val id: UserID,
    private val userRepository: UserRepository,
    private val credentialRepository: CredentialRepository<Credential>,
    private val articleRepository: ArticleRepository
) : User {

    override fun login(credential: Credential) {
        val user = credentialRepository.findUserByCredID(credential)
        if (user == null) {
            register(credential)
        }
    }

    private fun register(credential: Credential) {
        // TODO: тут нужна генерация имени
        val name = UserName(NotBlankString.orNull("test")!!)
        val saved = userRepository.save(
            UserSaveEntity(
                name = name,
                registrationTime = Clock.System.now()
            )
        )
        credentialRepository.save(
            CredentialEntity(userID = saved.id, credID = credential)
        )
    }

    override fun changeName(name: UserName) {
        userRepository
            .update(UserEditEntity(id = id, name = name))
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }
    }

    // TODO: сделать в транзакции
    override fun deleteAccount() {
        articleRepository.deleteAllByUserID(id)
        userRepository.deleteByUserID(id)
    }

}
