package dinf.data

import arrow.core.Either
import dinf.types.*
import java.time.Instant

interface UserRepository {

    fun findByUserID(userID: UserID): UserEntity?

    fun save(entity: UserSaveEntity): UserEntity

    fun update(entity: UserEditEntity): Either<EntityNotFoundError, UserEntity>

    fun deleteByUserID(userID: UserID)

}

enum class PermissionType {
    SIMPLE, ADMIN
}

data class UserEntity(
    val id: UserID,
    val name: UserName,
    val registrationTime: Instant,
    val permission: PermissionType
)

data class UserSaveEntity(
    val name: UserName,
    val registrationTime: Instant,
    val permission: PermissionType
)

data class UserEditEntity(
    val id: UserID,
    val name: UserName,
    val permission: PermissionType
)

fun RegisteredUser.toUserEditEntity(): UserEditEntity {
    val permission = when (this) {
        is AdminUser -> PermissionType.ADMIN
        is SimpleUser -> PermissionType.SIMPLE
    }
    return UserEditEntity(id = id, name = name, permission = permission)
}
