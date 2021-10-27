package dinf.domain

import arrow.core.getOrHandle
import dinf.data.*
import dinf.types.*
import kotlinx.datetime.Clock

class DBUser(
    private val id: UserID,
    private val userRepository: UserRepository,
    private val credentialRepository: CredentialRepository<Credential>,
    articleRepository: ArticleRepository
) : User {

    private val author: Author = DBAuthor(id, articleRepository)

    override fun login(credential: Credential) {
        val user = credentialRepository.findUserByCredID(credential)
        if (user == null) {
            register(credential)
        }
    }

    private fun register(credential: Credential) {
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

    override fun change(name: UserName) {
        userRepository
            .update(UserEditEntity(id = id, name = name))
            .getOrHandle { throw IllegalStateException("Found no user for id=$id") }
    }

    override fun deleteAccount() {
        author.deleteArticles()
        userRepository.deleteByUserID(id)
    }

}
