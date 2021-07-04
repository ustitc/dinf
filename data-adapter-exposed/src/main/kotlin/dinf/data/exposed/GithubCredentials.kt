package dinf.data.exposed

import org.jetbrains.exposed.sql.Table

object GithubCredentials : Table(name = "google_credentials") {

    val userID = reference("user_id", Users.id)
    val githubID = integer("github_id").uniqueIndex()

}
