package dinf.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object ArticleTable : IntIdTable(name = "articles") {

    val name = text("name")
    val description = text("description")
    val authorID = reference("author_id", UserTable.id)
    val creationTime = timestamp("creation_time")
    val lastUpdateTime = timestamp("last_update_time")
    val values = textArray("values")

}
