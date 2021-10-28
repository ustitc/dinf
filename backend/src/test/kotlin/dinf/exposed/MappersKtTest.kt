package dinf.exposed

import dinf.data.exposed.toUserName
import dinf.types.NotBlankString
import dinf.types.UserName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MappersKtTest : StringSpec({

    "string maps to UserName" {
        "John".toUserName() shouldBe UserName(NotBlankString.orNull("John")!!)
    }

})
