package dinf.data.exposed

import org.jetbrains.exposed.sql.Table

object GoogleCredentials  : Table(name = "github_credentials") {

    val userID = reference("user_id", Users.id)
    val googleID = text("google_id").uniqueIndex()

}
