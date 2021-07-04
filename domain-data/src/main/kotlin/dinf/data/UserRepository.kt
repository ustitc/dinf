package dinf.data

import arrow.core.Either
import dinf.types.UserID
import dinf.types.UserName
import java.time.Instant

interface UserRepository {

    fun save(entity: UserSaveEntity): UserEntity

    fun update(entity: UserEditEntity): Either<EntityNotFoundError, UserEntity>

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
