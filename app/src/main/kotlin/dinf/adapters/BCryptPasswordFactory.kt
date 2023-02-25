package dinf.adapters

import dinf.auth.PasswordFactory
import dinf.auth.Password
import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordFactory : PasswordFactory {

    private object Validation : (Password, String) -> Boolean {
        override fun invoke(p1: Password, p2: String): Boolean {
            return BCrypt.checkpw(p2, p1.hash)
        }
    }

    override fun fromPlaintext(text: String): Password {
        val hash = BCrypt.hashpw(text, BCrypt.gensalt())
        return fromHash(hash)
    }

    override fun fromHash(hash: String): Password {
        return Password(hash, Validation)
    }
}
