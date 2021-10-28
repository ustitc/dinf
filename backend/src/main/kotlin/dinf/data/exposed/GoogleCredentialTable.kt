package dinf.data.exposed

import org.jetbrains.exposed.sql.Table

object GoogleCredentialTable  : Table(name = "github_credentials") {

    val userID = reference("user_id", UserTable.id)
    val googleID = text("google_id").uniqueIndex()

}
