package dinf.html

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class JavascriptArrayTest : StringSpec({

    "constructs valid javascript array" {
        val array = JSStringArray(List(3) { HtmlContent.Simple(it) })

        array.print() shouldBe "[\"0\", \"1\", \"2\"]"
    }

    "constructs empty array if passed list is empty" {
        val array = JSStringArray(emptyList())

        array.print() shouldBe "[]"
    }

})
