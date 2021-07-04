package dinf.data.exposed

import org.jetbrains.exposed.sql.Table

object Permissions : Table(name = "permissions") {

    val userID = reference("user_id", Users.id).uniqueIndex()
    val type = text("type")

}
