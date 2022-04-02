package dinf.html.components

import kotlinx.html.A
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.role

fun FlowContent.loadingComponent(text: String, classes: String? = null, id: String? = null) {
    a(href = "#", classes = classes) {
        if (id != null) {
            this.id = id
        }
        attributes["aria-busy"] = "true"
        +text
    }
}

fun FlowContent.picoInlineButton(href: String = "#", classes: String? = null, block: A.() -> Unit) {
    a(href = href, classes = classes) {
        role = "button"
        block()
    }
}
