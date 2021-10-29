package dinf.backend

import dinf.exposed.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction

fun createUser(): UserEntity = transaction {
    UserEntity.new {
        name = "name"
    }
}
