package dinf.backend

import dinf.types.UserID
import dinf.types.UserName
import kotlinx.datetime.Instant

interface UserRepository {

    fun findByUserID(userID: UserID): UserEntity?

    fun save(entity: UserSaveEntity): UserEntity

    fun update(entity: UserEditEntity): Result<UserEntity>

    fun deleteByUserID(userID: UserID)

}

data class UserEntity(
    val id: UserID,
    val name: UserName,
    val registrationTime: Instant
)

data class UserSaveEntity(
    val name: UserName,
    val registrationTime: Instant
)

data class UserEditEntity(
    val id: UserID,
    val name: UserName
)
