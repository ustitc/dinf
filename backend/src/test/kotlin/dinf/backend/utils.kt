package dinf.backend

import dinf.domain.Content
import dinf.exposed.UserEntity
import dinf.types.NBString
import dinf.types.Values
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun createUser(): UserEntity = transaction {
    UserEntity.new {
        name = "name"
    }
}

suspend fun DBAuthor.createArticles(count: Int) = newSuspendedTransaction {
    for (i in 1..count) {
        createArticle(content())
    }
}

fun content(): Content {
    return Content(
        title = NBString.orNull("test")!!,
        description = "",
        values = Values.orThrow("test 1", "test 2")
    )
}
