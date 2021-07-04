package dinf.data.exposed

import dinf.data.PermissionEntity
import dinf.data.PermissionRepository
import dinf.data.PermissionType
import dinf.types.UserID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedPermissionRepository : PermissionRepository {

    override fun findByUserID(userID: UserID): PermissionEntity? = transaction {
        Permissions
            .select { Permissions.userID eq userID.toInt() }
            .map {
                PermissionEntity(
                    userID = UserID.orNull(it[Permissions.userID].value)!!,
                    type = PermissionType.valueOf(it[Permissions.type])
                )
            }
            .firstOrNull()
    }

    override fun saveOrUpdate(entity: PermissionEntity): Unit = transaction {
        val permission = findByUserID(entity.userID)
        if (permission == null) {
            Permissions.insert {
                it[userID] = entity.userID.toInt()
                it[type] = entity.type.toString()
            }
        } else {
            Permissions.update({ Permissions.userID eq entity.userID.toInt() }) {
                it[type] = entity.type.toString()
            }
        }
    }
}
