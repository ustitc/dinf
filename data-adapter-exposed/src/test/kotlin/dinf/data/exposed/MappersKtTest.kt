package dinf.data.exposed

import dinf.data.PermissionType
import dinf.types.NotBlankString
import dinf.types.UserName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MappersKtTest : StringSpec({

    "admin permission maps to 'admin'" {
        PermissionType.ADMIN.toSqlString() shouldBe "admin"
    }

    "simple permission maps to null" {
        PermissionType.SIMPLE.toSqlString() shouldBe null
    }

    "'admin' maps to admin permission" {
        "admin".toPermissionType() shouldBe PermissionType.ADMIN
    }

    "null maps to simple permission" {
        null.toPermissionType() shouldBe PermissionType.SIMPLE
    }

    "string maps to UserName" {
        "John".toUserName() shouldBe UserName(NotBlankString.orNull("John")!!)
    }

})
