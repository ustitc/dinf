package dinf.auth

import io.ktor.server.auth.*

sealed class Credential {

    data class EmailPassword(val email: String, val password: String) : Credential() {
        constructor(credential: UserPasswordCredential) : this(credential.name, credential.password)
    }

    data class Google(val token: String) : Credential()

}
