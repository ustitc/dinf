package dinf.app.auth

interface PasswordFactory {

    fun fromPlaintext(text: String): Password
    fun fromHash(hash: String): Password

}
