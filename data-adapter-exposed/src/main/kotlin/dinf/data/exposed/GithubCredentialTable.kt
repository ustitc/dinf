package dinf.data.exposed

import org.jetbrains.exposed.sql.Table

object GithubCredentialTable : Table(name = "google_credentials") {

    val userID = reference("user_id", UserTable.id)
    val githubID = integer("github_id").uniqueIndex()

}
