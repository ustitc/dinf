package dinf.domain

import dinf.data.*
import dinf.types.Credential
import dinf.types.NotBlankString
import dinf.types.UserName
import kotlinx.datetime.Clock

class DBAnonymousUser(
    private val credentialRepository: CredentialRepository<Credential>,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : AnonymousUser {

    override fun toLogined(credential: Credential): LoginedUser {
        val user = credentialRepository.findUserByCredID(credential) ?: register(credential)
        return DBUser(
            id = user.id,
            userRepository = userRepository,
            articleRepository = articleRepository
        )
    }

    private fun register(credential: Credential): UserEntity {
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
        return saved
    }

}
