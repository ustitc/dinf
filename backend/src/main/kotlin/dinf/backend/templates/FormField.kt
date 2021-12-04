package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.label

class FormField : Template<FlowContent> {

    var name: String = ""
    var help: String = ""
    val control = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        div("field") {
            label("label") {
                +name
            }
            div("control") {
                insert(control)
            }
            if (help.isNotBlank()) {
                div("help") {
                    +help
                }
            }
        }
    }
}
