package dinf.usecase

import arrow.core.getOrHandle
import dinf.data.*
import dinf.types.*
import java.time.Instant

class UserUseCasesImpl(
    private val userRepository: UserRepository,
    private val permissionRepository: PermissionRepository,
    private val credentialRepository: CredentialRepository<Credential>,
    private val articleUseCases: ArticleUseCases
) : UserUseCases {

    override fun AnonymousUser.login(credential: Credential): RegisteredUser {
        val user = credentialRepository.findUserByCredID(credential)
        return if (user == null) {
            register(credential)
        } else {
            val permissionEntity = permissionRepository.findByUserID(user.id)
            user.toRegisteredUser(permissionEntity)
        }
    }

    private fun register(credential: Credential): SimpleUser {
        // TODO: тут нужна генерация имени
        val name = UserName(NotBlankString.orNull("test")!!)
        val saved = userRepository.save(
            UserSaveEntity(name = name, registrationTime = Instant.now())
        )
        credentialRepository.save(
            CredentialEntity(userID = saved.id, credID = credential)
        )
        return SimpleUser(
            id = saved.id,
            name = saved.name
        )
    }

    private fun UserEntity.toRegisteredUser(pEntity: PermissionEntity?): RegisteredUser = when (pEntity) {
        null -> SimpleUser(id = id, name = name)
        else -> when (pEntity.type) {
            PermissionType.ADMIN -> AdminUser(id = id, name = name)
        }
    }

    override fun RegisteredUser.changeName(name: UserName): RegisteredUser =
        userRepository
            .update(
                UserEditEntity(id = id, name = name)
            )
            .map {
                when (this) {
                    is AdminUser -> AdminUser(id = it.id, name = it.name)
                    is SimpleUser -> SimpleUser(id = it.id, name = it.name)
                }
            }
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }

    override fun AdminUser.promoteToAdmin(user: SimpleUser): AdminUser {
        permissionRepository.saveOrUpdate(
            PermissionEntity(
                userID = user.id,
                type = PermissionType.ADMIN
            )
        )
        return AdminUser(id = user.id, name = user.name)
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
