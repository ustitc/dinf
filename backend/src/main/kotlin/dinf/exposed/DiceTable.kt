package dinf.exposed

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object DiceTable : LongIdTable(name = "dices") {

    val name = text("name")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val edges = text("edges")

}
