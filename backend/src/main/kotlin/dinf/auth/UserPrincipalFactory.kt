package dinf.auth

interface UserPrincipalFactory {

    fun create(name: String, email: String, password: Password): UserPrincipal

}
