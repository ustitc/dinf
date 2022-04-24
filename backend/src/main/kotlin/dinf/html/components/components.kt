package dinf.html.components

import kotlinx.html.A
import kotlinx.html.ARTICLE
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.article
import kotlinx.html.dialog
import kotlinx.html.div
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

fun FlowContent.picoHyperlinkAsButton(href: String = "#", classes: String? = null, block: A.() -> Unit) {
    a(href = href, classes = classes) {
        role = "button"
        block()
    }
}

fun FlowContent.picoInlineButton(classes: String? = null, block: DIV.() -> Unit) {
    div(classes = classes) {
        role = "button"
        block()
    }
}

fun FlowContent.picoModal(open: Boolean, block: ARTICLE.() -> Unit) {
    dialog {
        if (open) {
            attributes["open"] = "true"
        }
        article {
            block()
        }
    }
}
