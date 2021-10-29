package dinf.data.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.Instant

object UserTable : IntIdTable(name = "users") {

    val name = text("name")
    val registrationTime = timestamp("registration_time").clientDefault { Instant.now() }
    val githubCredential = integer("github_cred").nullable()
    val googleCredential = text("google_cred").nullable()

}
