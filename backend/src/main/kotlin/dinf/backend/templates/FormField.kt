package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.label
import kotlinx.html.small

class FormField : Template<FlowContent> {

    var name: String = ""
    var help: String = ""
    val control = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        label {
            +name
            insert(control)
            if (help.isNotBlank()) {
                small {
                    +help
                }
            }
        }
    }
}
