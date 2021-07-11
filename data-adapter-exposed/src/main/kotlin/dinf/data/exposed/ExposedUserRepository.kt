package dinf.data.exposed

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dinf.data.*
import dinf.types.UserID
import dinf.types.toPositiveInt
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedUserRepository : UserRepository {

    override fun findByUserID(userID: UserID): UserEntity? = transaction {
        val user = User.findById(userID.toInt())
        if (user != null) {
            UserEntity(
                id = UserID(user.id.value.toPositiveInt()!!),
                name = user.name.toUserName(),
                registrationTime = user.registrationTime.toKotlinInstant(),
                permission = user.permission.toPermissionType()
            )
        } else {
            null
        }
    }

    override fun save(entity: UserSaveEntity): UserEntity = transaction {
        val user = User.new {
            name = entity.name.toString()
            registrationTime = entity.registrationTime.toJavaInstant()
            permission = entity.permission.toSqlString()
        }
        UserEntity(
            id = UserID(user.id.value.toPositiveInt()!!),
            name = user.name.toUserName(),
            registrationTime = user.registrationTime.toKotlinInstant(),
            permission = user.permission.toPermissionType()
        )
    }

    override fun update(entity: UserEditEntity): Either<EntityNotFoundError, UserEntity> = transaction {
        val user = User.findById(entity.id.toInt())
        if (user == null) {
            EntityNotFoundError.left()
        } else {
            user.name = entity.name.toString()
            user.permission = entity.permission.toSqlString()

            UserEntity(
                id = UserID.orNull(user.id.value)!!,
                name = user.name.toUserName(),
                registrationTime = user.registrationTime.toKotlinInstant(),
                permission = user.permission.toPermissionType()
            ).right()
        }
    }

    override fun deleteByUserID(userID: UserID): Unit = transaction {
        User.findById(userID.toInt())?.delete()
    }
}
