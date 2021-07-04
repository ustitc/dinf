package dinf.data.exposed

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dinf.data.*
import dinf.types.*
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedUserRepository : UserRepository {

    override fun save(entity: UserSaveEntity): UserEntity = transaction {
        val user = User.new {
            name = entity.name.toString()
            registrationTime = entity.registrationTime
        }
        UserEntity(
            id = UserID(user.id.value.toPositiveInt()!!),
            name = UserName(
                NotBlankString.orNull(user.name)!!
            ),
            registrationTime = user.registrationTime
        )
    }

    override fun update(entity: UserEditEntity): Either<EntityNotFoundError, UserEntity> {
        val user = User.findById(entity.id.toInt())
        return if (user == null) {
            EntityNotFoundError.left()
        } else {
            user.name = entity.name.toString()

            UserEntity(
                id = UserID.orNull(user.id.value)!!,
                name = UserName(NotBlankString.orNull(user.name)!!),
                registrationTime = user.registrationTime
            ).right()
        }
    }

    override fun deleteByUserID(userID: UserID) {
        User.findById(userID.toInt())?.delete()
    }
}
