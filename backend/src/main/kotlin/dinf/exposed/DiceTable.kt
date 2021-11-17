package dinf.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object DiceTable : IntIdTable(name = "dices") {

    val name = text("name")
    val creationTime = timestamp("creation_time")
    val lastUpdateTime = timestamp("last_update_time")
    val edges = textArray("edges")

}
