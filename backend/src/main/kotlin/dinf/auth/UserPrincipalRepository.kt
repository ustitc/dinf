package dinf.auth

interface UserPrincipalRepository {

    fun findByEmailOrNull(email: String): UserPrincipal?

}
