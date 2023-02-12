package dinf.auth

import dinf.domain.ID
import dinf.domain.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UserPrincipalTest : StringSpec({

    "has same passwords" {
        val userPrincipal = UserPrincipal(
            user = User(ID.first(), "John"),
            password = Password("hash") { _, _ -> true }
        )

        userPrincipal.hasSamePassword("hash") shouldBe true
    }

    "has different passwords" {
        val userPrincipal = UserPrincipal(
            user = User(ID.first(), "John"),
            password = Password("hash") { _, _ -> false }
        )

        userPrincipal.hasSamePassword("hash") shouldBe false
    }

    "has no password" {
        val userPrincipal = UserPrincipal(
            user = User(ID.first(), "John"),
            password = null
        )

        userPrincipal.hasSamePassword("hash") shouldBe false
    }

})
