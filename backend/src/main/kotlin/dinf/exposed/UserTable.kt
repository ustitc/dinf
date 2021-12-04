package dinf.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserTable : IntIdTable(name = "users") {

    val name = text("name")
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val githubCredential = integer("github_cred").nullable()
    val googleCredential = text("google_cred").nullable()

}
