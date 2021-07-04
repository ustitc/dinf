package dinf.data.exposed

import dinf.data.CredentialRepository
import dinf.data.UserEntity
import dinf.types.Credential
import dinf.types.NotBlankString
import dinf.types.UserID
import dinf.types.UserName
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
                    name = UserName(NotBlankString.orNull(it[Users.name])!!),
                    registrationTime = it[Users.registrationTime]
                )
            }
            .firstOrNull()
    }
}