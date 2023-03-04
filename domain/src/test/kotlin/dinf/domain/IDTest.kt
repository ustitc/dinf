package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IDTest : StringSpec({

    "return number id as as string" {
        val result = ID(10).print()

        result shouldBe "10"
    }

})
