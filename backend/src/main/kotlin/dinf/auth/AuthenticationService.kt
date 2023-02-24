package dinf.auth

import dinf.domain.UserFactory

class AuthenticationService(
    private val emailPasswordsService: EmailPasswordsService,
    private val oAuthService: OAuthService,
    private val userFactory: UserFactory,
    private val nameSource: () -> String
) {

    suspend fun login(credential: Credential): CredentialValidation {
        return findUserAndValidateCredential(credential)
    }

    sealed class Registration {

        data class Created(val userPrincipal: UserPrincipal) : Registration()
        object Exists : Registration()
    }

    suspend fun register(credential: Credential): Registration {
        return when (findUserAndValidateCredential(credential)) {
            is CredentialValidation.Ok -> Registration.Exists
            is CredentialValidation.Invalid -> {
                if (credential is Credential.Google) {
                    error("Bad credentials=$credential passed for Google auth")
                }
                return Registration.Exists
            }
            is CredentialValidation.NotFound -> {
                val createdUser = userFactory.create(nameSource.invoke())

                when (credential) {
                    is Credential.Google -> oAuthService.addLogin(createdUser, credential)
                    is Credential.EmailPassword -> emailPasswordsService.addLogin(createdUser, credential)
                }
                Registration.Created(UserPrincipal(createdUser))
            }
        }
    }

    private suspend fun findUserAndValidateCredential(credential: Credential): CredentialValidation {
        return when (credential) {
            is Credential.Google -> oAuthService.findUser(credential)
            is Credential.EmailPassword -> emailPasswordsService.findUser(credential)
        }
    }

}
