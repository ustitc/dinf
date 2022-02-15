package dinf.html

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EscapedStringTest : StringSpec({

    "html tag is escaped" {
        val str = EscapedString("<button>")

        str.print() shouldBe "&lt;button&gt;"
    }

    "non html tag is not escaped" {
        val str = EscapedString("hey!")

        str.print() shouldBe "hey!"
    }

})
