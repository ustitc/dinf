package dinf.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiceEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<DiceEntity>(DiceTable)

    var name by DiceTable.name
    var createdAt by DiceTable.createdAt
    var updatedAt by DiceTable.updatedAt
    var edges by DiceTable.edges

}
