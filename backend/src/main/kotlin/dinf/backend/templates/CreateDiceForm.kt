package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.input
import kotlinx.html.textArea

class CreateDiceForm(private val form: Form) : Template<FlowContent> {

    override fun FlowContent.apply() {
        insert(form) {
            field {
                name = "Name"
                control {
                    input(classes = "input", name = "name", type = InputType.text) {
                        required = true
                    }
                }
            }
            field {
                name = "Edges"
                help = "Each value must be on new line"
                control {
                    textArea(classes = "textarea") {
                        name = "edges"
                        required = true
                    }
                }
            }
            submit {
                value = "Save"
            }
        }
    }
}
