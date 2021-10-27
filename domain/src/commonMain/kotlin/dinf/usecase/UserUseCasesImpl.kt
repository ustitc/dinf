package dinf.usecase

import arrow.core.getOrHandle
import dinf.data.*
import dinf.types.*
import kotlinx.datetime.Clock

class UserUseCasesImpl(
    private val userRepository: UserRepository,
    private val credentialRepository: CredentialRepository<Credential>,
    private val articleUseCases: ArticleUseCases
) : UserUseCases {

    override fun AnonymousUser.login(credential: Credential): RegisteredUser {
        val user = credentialRepository
            .findUserByCredID(credential)
            ?.let { RegisteredUser(id = it.id, name = it.name) }
        return user ?: register(credential)
    }

    private fun register(credential: Credential): RegisteredUser {
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
        return RegisteredUser(
            id = saved.id,
            name = saved.name
        )
    }

    override fun RegisteredUser.changeName(name: UserName): RegisteredUser {
        return userRepository
            .update(this.toUserEditEntity())
            .map { RegisteredUser(it.id, it.name) }
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }
    }


    override fun RegisteredUser.deleteAccount(): AnonymousUser {
        articleUseCases.run {
            deleteAllOwnArticles()
        }
        userRepository.deleteByUserID(id)
        return AnonymousUser
    }

}
