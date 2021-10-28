package dinf.data.exposed

import dinf.backend.CredentialRepository
import dinf.backend.UserEntity
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
        (table innerJoin UserTable)
            .select { findUserByCredIDCondition(credID) }
            .map {
                UserEntity(
                    id = UserID.orNull(it[UserTable.id].value)!!,
                    name = it[UserTable.name].toUserName(),
                    registrationTime =  it[UserTable.registrationTime].toKotlinInstant()
                )
            }
            .firstOrNull()
    }
}
