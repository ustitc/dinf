package dinf.auth

interface UserPrincipalRepository {

    fun findByEmailOrNull(email: String): UserPrincipal?

    class InMemory(private val map: Map<String, UserPrincipal> = mapOf()) : UserPrincipalRepository {
        override fun findByEmailOrNull(email: String): UserPrincipal? {
            return map[email]
        }
    }

}
