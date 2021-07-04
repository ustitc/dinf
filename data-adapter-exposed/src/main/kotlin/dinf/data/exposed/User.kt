package dinf.data.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<User>(Users)
    var name by Users.name
    var permission by Users.permission
    var registrationTime by Users.registrationTime

}
