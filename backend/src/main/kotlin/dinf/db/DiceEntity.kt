package dinf.db

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiceEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<DiceEntity>(DiceTable)

    var name by DiceTable.name
    var createdAt by DiceTable.createdAt
    var updatedAt by DiceTable.updatedAt
    var edges by DiceTable.edges

}
