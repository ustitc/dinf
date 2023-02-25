package dinf.config

data class LoginConfig(val password: Password = Password(), val oauth: OAuth = OAuth()) {

    data class Password(val enabled: Boolean = false)

    data class OAuth(val google: Google = Google()) {

        data class Google(
            val enabled: Boolean = false,
            val clientId: String = "",
            val clientSecret: String = ""
        )
    }
}
