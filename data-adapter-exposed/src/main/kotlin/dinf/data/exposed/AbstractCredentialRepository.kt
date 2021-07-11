package dinf.data.exposed

import dinf.data.CredentialRepository
import dinf.data.UserEntity
import dinf.types.Credential
import dinf.types.UserID
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

abstract class AbstractCredentialRepository<T : Credential>(
    private val table: Table
) : CredentialRepository<T> {

    abstract val findUserByCredIDCondition: (T) -> Op<Boolean>

    override fun findUserByCredID(credID: T): UserEntity? = transaction {
        (table innerJoin Users)
            .select { findUserByCredIDCondition(credID) }
            .map {
                UserEntity(
                    id = UserID.orNull(it[Users.id].value)!!,
                    name = it[Users.name].toUserName(),
                    registrationTime =  it[Users.registrationTime].toKotlinInstant(),
                    permission = it[Users.permission].toPermissionType()
                )
            }
            .firstOrNull()
    }
}
