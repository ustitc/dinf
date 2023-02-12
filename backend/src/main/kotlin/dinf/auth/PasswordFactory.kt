package dinf.auth

interface PasswordFactory {

    fun fromPlaintext(text: String): Password
    fun fromHash(hash: String): Password

    class Stub(
        private val block: () -> Password = { Password("hash") { _, _ -> true } }
    ) : PasswordFactory {

        override fun fromPlaintext(text: String): Password {
            return block()
        }

        override fun fromHash(hash: String): Password {
            return block()
        }
    }
}
