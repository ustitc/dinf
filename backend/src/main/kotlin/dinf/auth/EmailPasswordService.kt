package dinf.auth

import dinf.db.firstOrNull
import dinf.db.sql
import dinf.db.transaction
import dinf.domain.ID
import dinf.domain.User
import dinf.domain.UserFactory
import dinf.types.toPLong

class EmailPasswordService(
    private val userFactory: UserFactory,
    private val nameSource: () -> String,
    private val passwordFactory: PasswordFactory
) {

    sealed class Registration {

        data class Created(val userPrincipal: UserPrincipal) : Registration()
        object Exists : Registration()
    }

    fun register(credential: Credential.EmailPassword): Registration {
        val stored = find(credential)
        if (stored != null) {
            return Registration.Exists
        }
        val user = userFactory.create(name = nameSource.invoke())
        addLogin(user, credential)
        return Registration.Created(UserPrincipal(user))
    }

    private fun addLogin(user: User, login: Credential.EmailPassword) {
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

    fun login(credential: Credential.EmailPassword): EmailPasswordValidation {
        val stored = find(credential) ?: return EmailPasswordValidation.NotFound

        val storedPassword = passwordFactory.fromHash(stored.passwordHash)
        if (storedPassword.isSame(credential.password)) {
            return EmailPasswordValidation.Ok(
                UserPrincipal(
                    User(
                        ID(stored.id.toPLong()),
                        stored.name
                    )
                )

            )
        }
        return EmailPasswordValidation.Invalid
    }

    private fun find(credential: Credential.EmailPassword): StoredCredentials? {
        return sql(
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
        }
    }

    private data class StoredCredentials(val id: Long, val name: String, val passwordHash: String)

}
