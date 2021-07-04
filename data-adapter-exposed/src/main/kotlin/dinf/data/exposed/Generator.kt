package dinf.data.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Generator(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Generator>(Generators)

    var name by Generators.name
    var description by Generators.description
    var author by User referencedOn Generators.authorID
    var creation by Generators.creationTime
    var lastUpdate by Generators.lastUpdateTime
    var values by Generators.values

}
