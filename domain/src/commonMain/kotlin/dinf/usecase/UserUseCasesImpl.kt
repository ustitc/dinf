package dinf.usecase

import arrow.core.Either
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
            ?.toRegisteredUser()
        return user ?: register(credential)
    }

    private fun register(credential: Credential): SimpleUser {
        // TODO: тут нужна генерация имени
        val name = UserName(NotBlankString.orNull("test")!!)
        val saved = userRepository.save(
            UserSaveEntity(
                name = name,
                registrationTime = Clock.System.now(),
                permission = PermissionType.SIMPLE
            )
        )
        credentialRepository.save(
            CredentialEntity(userID = saved.id, credID = credential)
        )
        return SimpleUser(
            id = saved.id,
            name = saved.name
        )
    }

    private fun UserEntity.toRegisteredUser(): RegisteredUser = when (permission) {
        PermissionType.ADMIN -> AdminUser(id = id, name = name)
        PermissionType.SIMPLE -> SimpleUser(id = id, name = name)
    }

    override fun RegisteredUser.changeName(name: UserName): RegisteredUser {
        return userRepository
            .update(this.toUserEditEntity())
            .map {
                when (this) {
                    is AdminUser -> AdminUser(id = it.id, name = it.name)
                    is SimpleUser -> SimpleUser(id = it.id, name = it.name)
                }
            }
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }
    }

    override fun AdminUser.promoteToAdmin(user: SimpleUser): Either<UserNotFoundError, AdminUser> {
        val entity = this.toUserEditEntity().copy(permission = PermissionType.ADMIN)
        return userRepository
            .update(entity)
            .map { AdminUser(id = user.id, name = user.name) }
            .mapLeft { UserNotFoundError }
    }

    override fun RegisteredUser.deleteAccount(): AnonymousUser {
        articleUseCases.run {
            deleteAllOwnArticles()
        }
        userRepository.deleteByUserID(id)
        return AnonymousUser
    }

    override fun AdminUser.deleteAccount(user: SimpleUser) {
        user.run {
            deleteAccount()
        }
    }
}
