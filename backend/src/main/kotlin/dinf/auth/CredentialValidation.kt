package dinf.auth

sealed class CredentialValidation {

    class Ok(val userPrincipal: UserPrincipal) : CredentialValidation()
    object Invalid : CredentialValidation()
    object NotFound : CredentialValidation()

}
