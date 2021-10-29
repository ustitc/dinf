package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.exposed.postgresTestListeners
import dinf.types.GithubCredential
import dinf.types.PInt
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction

class DBAnonymousUserTest : StringSpec({

    listeners(postgresTestListeners)

    val anonymous = DBAnonymousUser()

    "creates new user in DB if it hasn't been created yet" {
        anonymous.toLogined(GithubCredential(PInt.orNull(123)!!))
        transaction { UserEntity.count() } shouldBe 1L
    }

    "doesn't create new user if it has been found" {
        anonymous.toLogined(GithubCredential(PInt.orNull(123)!!))
        anonymous.toLogined(GithubCredential(PInt.orNull(123)!!))
        transaction { UserEntity.count() } shouldBe 1L
    }

})
