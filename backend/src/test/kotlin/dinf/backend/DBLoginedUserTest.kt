package dinf.backend

import dinf.exposed.ArticleEntity
import dinf.exposed.UserEntity
import dinf.exposed.UserTable
import dinf.domain.Content
import dinf.exposed.postgresTestListeners
import dinf.types.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction

class DBLoginedUserTest : StringSpec({

    listeners(postgresTestListeners)

    "name has been changed" {
        val entity = createUser()
        val user = DBLoginedUser(entity)

        user.change(UserName.orThrow("new"))

        transaction {
            UserEntity.find { UserTable.name eq "new" }.count()
        } shouldBe 1L
    }

    "user and his articles has been deleted" {
        val entity = createUser()
        val user = DBLoginedUser(entity)
        val author = DBAuthor(entity)
        author.createArticle(
            Content(
                title = NBString.orNull("test")!!,
                description = "",
                values = Values.orThrow("test 1", "test 2")
            )
        )
        transaction { UserEntity.count() } shouldBe 1L
        transaction { ArticleEntity.count() } shouldBe 1L

        user.deleteAccount()

        transaction { UserEntity.count() } shouldBe 0L
        transaction { ArticleEntity.count() } shouldBe 0L
    }

})
