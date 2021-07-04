package dinf.data.exposed

import dinf.exposed.textArray
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Generators : IntIdTable(name = "generator") {

    val name = text("name")
    val description = text("description")
    val authorID = reference("author_id", Users.id)
    val creationTime = timestamp("creation_time")
    val lastUpdateTime = timestamp("last_update_time")
    val values = textArray("values")

}
