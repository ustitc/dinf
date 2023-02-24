package dinf.auth

import dinf.db.firstOrNull
import dinf.db.sql
import dinf.db.transaction
import dinf.domain.ID
import dinf.domain.User
import dinf.types.toPLong

class EmailPasswordsService(private val passwordFactory: PasswordFactory) {

    fun addLogin(user: User, login: Credential.EmailPassword) {
        val password = passwordFactory.fromPlaintext(login.password)
        return transaction {
            prepareStatement("INSERT INTO login_email_passwords (password, user) VALUES (?, ?)")
                .use {
                    it.setString(1, password.hash)
                    it.setLong(2, user.id.toLong())
                    it.execute()
                }
        }
    }

    fun findUser(credential: Credential.EmailPassword): CredentialValidation {
        val stored = sql(
            """
            SELECT users.id, users.name, login_email_passwords.password 
            FROM users 
            JOIN login_email_passwords ON users.id = login_email_passwords.user 
            WHERE login_email_passwords.email = ?
            """
        ) {
            setString(1, credential.email)
            executeQuery().firstOrNull {
                StoredCredentials(
                    id = getLong("id"),
                    name = getString("name"),
                    passwordHash = getString("password")
                )
            }
        } ?: return CredentialValidation.NotFound

        val storedPassword = passwordFactory.fromHash(stored.passwordHash)
        if (storedPassword.isSame(credential.password)) {
            return CredentialValidation.Ok(
                UserPrincipal(
                    User(
                        ID(stored.id.toPLong()),
                        stored.name
                    )
                )

            )
        }
        return CredentialValidation.Invalid
    }

    private data class StoredCredentials(val id: Long, val name: String, val passwordHash: String)

}
