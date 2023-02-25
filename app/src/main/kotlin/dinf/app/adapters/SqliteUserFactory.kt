package dinf.app.adapters

import dinf.app.db.first
import dinf.app.db.getPLong
import dinf.app.db.transaction
import dinf.domain.ID
import dinf.domain.User
import dinf.domain.UserFactory
import java.sql.ResultSet

class SqliteUserFactory : UserFactory {

    override fun create(name: String): User {
        return transaction {
            prepareStatement(
                """
                INSERT INTO users (name) VALUES (?) 
                RETURNING id, name
                """
            ).use {
                it.setString(1, name)
                it.executeQuery().first {
                    toUser(this)
                }
            }
        }
    }

    private fun toUser(rs: ResultSet): User {
        return User(
            id = ID(rs.getPLong("id")),
            name = rs.getString("name")
        )
    }
}
