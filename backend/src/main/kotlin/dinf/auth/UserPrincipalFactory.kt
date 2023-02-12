package dinf.auth

import dinf.domain.ID
import dinf.domain.User

interface UserPrincipalFactory {

    fun create(name: String, email: String, password: Password): UserPrincipal

    class Stub(
        private val block: () -> UserPrincipal = { UserPrincipal(User(ID.first(), "Happy User"), null) }
    ) : UserPrincipalFactory {
        override fun create(name: String, email: String, password: Password): UserPrincipal {
            return block()
        }
    }

}
