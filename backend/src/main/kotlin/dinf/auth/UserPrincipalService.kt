package dinf.auth

import io.ktor.server.auth.*

class UserPrincipalService(
    private val repo: UserPrincipalRepository,
    private val factory: UserPrincipalFactory,
    private val passwordFactory: PasswordFactory,
    private val nameSource: () -> String
) {

    sealed class CreateResult {

        class Created(val principal: UserPrincipal) : CreateResult()
        object AlreadyExists : CreateResult()

    }

    fun find(credential: UserPasswordCredential): UserPrincipal? {
        return repo.findByEmailOrNull(credential.name)
            ?.takeIf { it.hasSamePassword(credential.password) }
    }

    fun createUser(email: String, password: String): CreateResult {
        val principal = repo.findByEmailOrNull(email)
        return if (principal == null) {
            val passwordHash = passwordFactory.fromPlaintext(password)
            val newPrincipal = factory.create(
                name = nameSource.invoke(),
                email = email,
                password = passwordHash
            )
            CreateResult.Created(newPrincipal)
        } else {
            CreateResult.AlreadyExists
        }
    }
}
