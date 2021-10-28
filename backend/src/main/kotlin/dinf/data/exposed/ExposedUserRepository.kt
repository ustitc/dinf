package dinf.data.exposed

import dinf.backend.*
import dinf.types.UserID
import dinf.types.toPositiveInt
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedUserRepository : UserRepository {

    override fun findByUserID(userID: UserID): dinf.backend.UserEntity? = transaction {
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

    override fun save(entity: UserSaveEntity): dinf.backend.UserEntity = transaction {
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

    override fun update(entity: UserEditEntity): Result<dinf.backend.UserEntity> = transaction {
        val id = entity.id.toInt()
        val user = UserEntity.findById(id)
        if (user == null) {
            Result.failure(
                IllegalStateException("Can't update user with id=$id. Entry doesn't exist")
            )
        } else {
            user.name = entity.name.toString()

            Result.success(
                UserEntity(
                    id = UserID.orNull(user.id.value)!!,
                    name = user.name.toUserName(),
                    registrationTime = user.registrationTime.toKotlinInstant()
                )
            )
        }
    }

    override fun deleteByUserID(userID: UserID): Unit = transaction {
        UserEntity.findById(userID.toInt())?.delete()
    }
}
