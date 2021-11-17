package dinf.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiceEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<DiceEntity>(DiceTable)

    var name by DiceTable.name
    var creation by DiceTable.creationTime
    var lastUpdate by DiceTable.lastUpdateTime
    var edges by DiceTable.edges

}
