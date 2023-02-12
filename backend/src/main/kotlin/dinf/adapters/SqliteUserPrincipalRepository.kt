package dinf.adapters

import dinf.auth.PasswordFactory
import dinf.auth.UserPrincipal
import dinf.auth.UserPrincipalRepository
import dinf.db.firstOrNull
import dinf.db.getPLong
import dinf.db.sql
import dinf.domain.ID
import dinf.domain.User
import java.sql.ResultSet

class SqliteUserPrincipalRepository(private val passwordFactory: PasswordFactory) : UserPrincipalRepository {

    override fun findByEmailOrNull(email: String): UserPrincipal? {
        return sql(
            """
            SELECT users.id, users.name, user_passwords.password 
            FROM users 
            JOIN user_passwords ON users.id = user_passwords.user 
            WHERE users.email = ?
            """
        ) {
            setString(1, email)
            executeQuery().firstOrNull {
                toPrincipal(this)
            }
        }
    }

    private fun toPrincipal(rs: ResultSet): UserPrincipal {
        return UserPrincipal(
            user = User(
                id = ID(rs.getPLong("id")),
                name = rs.getString("name")
            ),
            password = passwordFactory.fromHash(
                rs.getString("password")
            )
        )
    }
}
