package dinf.backend

import dinf.exposed.DiceEntity
import dinf.exposed.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

fun createDiceEntity(): DiceEntity = transaction {
    DiceEntity.new {
        name = "test"
        createdAt = Instant.now()
        updatedAt = Instant.now()
        edges = arrayOf("1", "2", "3")
    }
}

fun createUser(): UserEntity = transaction {
    UserEntity.new {
        name = "name"
    }
}
