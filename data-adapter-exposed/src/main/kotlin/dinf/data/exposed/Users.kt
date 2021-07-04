package dinf.data.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Users : IntIdTable(name = "users") {

    val name = text("name")
    val registrationTime = timestamp("registration_time")

}
