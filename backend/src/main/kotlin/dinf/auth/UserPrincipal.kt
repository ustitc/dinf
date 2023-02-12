package dinf.auth

import dinf.domain.User
import io.ktor.server.auth.*

class UserPrincipal(
    val user: User,
    val password: Password? = null
) : Principal {

    val session get() = UserSession(id = user.id.number.toLong(), name = user.name)

    fun hasSamePassword(plaintextPassword: String): Boolean {
        return password != null && password.isSame(plaintextPassword)
    }

}
