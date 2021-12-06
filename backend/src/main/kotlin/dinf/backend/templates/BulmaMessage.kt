package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.article
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.p

class BulmaMessage : Template<FlowContent> {

    var header: String = ""
    var hasDeleteButton = false
    var color: BulmaColor? = null
    val body = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        article("message ${color?.value ?: ""}") {
            div("message-header") {
                if (header.isNotBlank()) {
                    p { +header }
                }
                if (hasDeleteButton) {
                    button(classes = "delete") { }
                }
            }
            div("message-body") {
                insert(body)
            }
        }
    }
}
