package dinf.data.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object UserTable : IntIdTable(name = "users") {

    val name = text("name")
    val registrationTime = timestamp("registration_time")

}
