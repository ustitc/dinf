package dinf.auth

import dinf.domain.User
import io.ktor.server.auth.*

class UserPrincipal(val user: User) : Principal {

    val session get() = UserSession(id = user.id.number.toLong(), name = user.name)
}
