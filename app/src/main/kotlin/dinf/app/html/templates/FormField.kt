package dinf.app.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.label
import kotlinx.html.small

class FormField : Template<FlowContent> {

    var name: String = ""
    var help: String = ""
    val control = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        if (name.isNotBlank()) {
            label {
                +name
            }
        }
        insert(control)
        if (help.isNotBlank()) {
            small {
                +help
            }
        }
    }
}
