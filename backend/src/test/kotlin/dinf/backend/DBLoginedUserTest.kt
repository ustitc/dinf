package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.data.exposed.UserTable
import dinf.exposed.postgresTestListeners
import dinf.types.*
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction

class DBLoginedUserTest : StringSpec({

    listeners(postgresTestListeners)

    val anonymous = DBAnonymousUser(ArticleRepository.Stub())

    "name has been changed" {
        val user = anonymous.toLogined(GithubCredential(PInt.orNull(123)!!))

        user.change(UserName(NotBlankString.orNull("new")!!))

        transaction {
            UserEntity.find { UserTable.name eq "new" }.count()
        } shouldBe 1L
    }

    "error on name change if entity doesn't exist" {
        val user = DBLoginedUser(userID = UserID.orNull(123)!!, articleRepository = ArticleRepository.Stub())

        shouldThrowAny {
            user.change(UserName(NotBlankString.orNull("new")!!))
        }
    }

    // TODO: needs article delete implementation
//    "user has been deleted" {
//        val user = anonymous.toLogined(GithubCredential(PInt.orNull(123)!!))
//        transaction { UserEntity.count() } shouldBe 1L
//
//        user.deleteAccount()
//
//        transaction { UserEntity.count() } shouldBe 0L
//    }

})
