package dinf.adapters

import dinf.auth.Password
import dinf.auth.UserPrincipal
import dinf.auth.UserPrincipalFactory
import dinf.db.first
import dinf.db.getPLong
import dinf.db.setPLong
import dinf.db.transaction
import dinf.domain.ID
import dinf.domain.User
import java.sql.Connection
import java.sql.ResultSet

class SqliteUserPrincipalFactory : UserPrincipalFactory {

    override fun create(name: String, email: String, password: Password): UserPrincipal {
        return transaction {
            val user = createUser(name, email)
            linkPassword(user.id, password)
            UserPrincipal(user, password)
        }
    }

    private fun Connection.createUser(name: String, email: String): User {
        return prepareStatement(
            """
            INSERT INTO users (name, email) VALUES (?, ?) 
            RETURNING id, name, email
            """
        ).use {
            it.setString(1, name)
            it.setString(2, email)
            it.executeQuery().first {
                toUser(this)
            }
        }
    }

    private fun Connection.linkPassword(userId: ID, password: Password) {
        prepareStatement(
            """
            INSERT INTO user_passwords (password, user) VALUES (?, ?) 
            """
        ).use {
            it.setString(1, password.hash)
            it.setPLong(2, userId.number)
            it.execute()
        }
    }

    private fun toUser(rs: ResultSet): User {
        return User(
            id = ID(rs.getPLong("id")),
            name = rs.getString("name")
        )
    }
}
