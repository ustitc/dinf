package dinf

import dinf.db.DiceEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

fun createDiceEntity(): DiceEntity = transaction {
    DiceEntity.new {
        name = "test"
        createdAt = Instant.now()
        updatedAt = Instant.now()
        edges = "1\n2\n3"
    }
}
