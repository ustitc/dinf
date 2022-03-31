package dinf.html.components

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id

fun FlowContent.loadingComponent(text: String, classes: String? = null, id: String? = null) {
    a(href = "#", classes = classes) {
        if (id != null) {
            this.id = id
        }
        attributes["aria-busy"] = "true"
        +text
    }
}
