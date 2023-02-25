package dinf.app.auth

class Password(
    val hash: String,
    private val validate: (Password, String) -> Boolean
) {

    fun isSame(plain: String): Boolean {
        return validate.invoke(this, plain)
    }

}
