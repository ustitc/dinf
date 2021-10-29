package dinf.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<UserEntity>(UserTable)
    var name by UserTable.name
    var registrationTime by UserTable.registrationTime
    var githubCredential by UserTable.githubCredential
    var googleCredential by UserTable.googleCredential

}
