package dinf.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object DiceTable : IntIdTable(name = "dices") {

    val name = text("name")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val edges = text("edges")

}
