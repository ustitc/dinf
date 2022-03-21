package dinf.html

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class HTMLTextWithNewLinesTest : StringSpec({

    "new lines are escaped" {
        val text = """
            1
            2
            3
        """.trimIndent()
        val content = HTMLTextWithNewLines(text)

        content.print() shouldBe "1&#13;&#10;2&#13;&#10;3"
    }

    "works with carriages and new lines" {
        val content = HTMLTextWithNewLines("1\r\n2\r\n3")

        content.print() shouldBe "1&#13;&#10;2&#13;&#10;3"
    }

    "text without new lines is not escaped" {
        val text = "text"
        val content = HTMLTextWithNewLines(text)

        content.print() shouldBe text
    }

})
