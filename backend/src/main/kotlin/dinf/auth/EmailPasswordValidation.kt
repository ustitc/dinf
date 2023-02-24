package dinf.auth

sealed class EmailPasswordValidation {

    class Ok(val userPrincipal: UserPrincipal) : EmailPasswordValidation()
    object Invalid : EmailPasswordValidation()
    object NotFound : EmailPasswordValidation()

}
