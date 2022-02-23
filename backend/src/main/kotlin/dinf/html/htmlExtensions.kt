package dinf.html

import dinf.types.NBString
import kotlinx.html.HTMLTag
import kotlinx.html.Tag

fun HTMLTag.dataAttribute(name: String, value: HtmlContent) {
    attributes[name] = value.print()
}

fun Tag.text(s: NBString) {
    text(s.toString())
}
