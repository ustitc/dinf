package dinf.backend

import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.ints.Positive
import dinf.exposed.UserEntity
import dinf.exposed.postgresTestListeners
import dinf.types.GithubCredential
import dinf.types.PInt
import dinf.types.PositiveInt
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction

class DBAnonymousUserTest : StringSpec({

    listeners(postgresTestListeners)

    val anonymous = DBAnonymousUser()

    "creates new user in DB if it hasn't been created yet" {
        anonymous.toLogined(
            GithubCredential(
                PInt(123 refined Positive())
            )
        )
        transaction { UserEntity.count() } shouldBe 1L
    }

    "doesn't create new user if it has been found" {
        anonymous.toLogined(
            GithubCredential(
                PInt(123 refined Positive())
            )
        )
        anonymous.toLogined(
            GithubCredential(
                PositiveInt(123 refined Positive())
            )
        )
        transaction { UserEntity.count() } shouldBe 1L
    }

})
