package dinf.data

import dinf.types.UserID

interface PermissionRepository {

    fun findByUserID(userID: UserID): PermissionEntity?

    fun saveOrUpdate(entity: PermissionEntity)

}

data class PermissionEntity(
    val userID: UserID,
    val type: PermissionType
)

enum class PermissionType {
    ADMIN
}