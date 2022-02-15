package dinf.html

import kotlinx.html.HTMLTag

fun HTMLTag.dataAttribute(name: String, value: HtmlContent) {
    attributes[name] = value.print()
}
