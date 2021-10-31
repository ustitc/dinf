package dinf.types

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValuesTest : StringSpec({

    "returns string array" {
        val values = Values("test 1", "test 2", "test 3")

        values.stringArray() shouldBe arrayOf("test 1", "test 2", "test 3")
    }

})
