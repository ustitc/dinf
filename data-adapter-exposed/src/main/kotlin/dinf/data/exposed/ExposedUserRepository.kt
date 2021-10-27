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

    override fun findByUserID(userID: UserID): dinf.data.UserEntity? = transaction {
        val user = UserEntity.findById(userID.toInt())
        if (user != null) {
            UserEntity(
                id = UserID(user.id.value.toPositiveInt()!!),
                name = user.name.toUserName(),
                registrationTime = user.registrationTime.toKotlinInstant()
            )
        } else {
            null
        }
    }

    override fun save(entity: UserSaveEntity): dinf.data.UserEntity = transaction {
        val user = UserEntity.new {
            name = entity.name.toString()
            registrationTime = entity.registrationTime.toJavaInstant()
        }
        UserEntity(
            id = UserID(user.id.value.toPositiveInt()!!),
            name = user.name.toUserName(),
            registrationTime = user.registrationTime.toKotlinInstant()
        )
    }

    override fun update(entity: UserEditEntity): Either<EntityNotFoundError, dinf.data.UserEntity> = transaction {
        val user = UserEntity.findById(entity.id.toInt())
        if (user == null) {
            EntityNotFoundError.left()
        } else {
            user.name = entity.name.toString()

            UserEntity(
                id = UserID.orNull(user.id.value)!!,
                name = user.name.toUserName(),
                registrationTime = user.registrationTime.toKotlinInstant()
            ).right()
        }
    }

    override fun deleteByUserID(userID: UserID): Unit = transaction {
        UserEntity.findById(userID.toInt())?.delete()
    }
}
