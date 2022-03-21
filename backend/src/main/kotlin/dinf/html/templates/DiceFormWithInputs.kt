package dinf.html.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.input
import kotlinx.html.textArea

class DiceFormWithInputs(private val form: Form) : DiceForm {

    override var name: String = ""
    override var edges: List<String> = emptyList()
    override var failed: Boolean = false

    override fun FlowContent.apply() {
        insert(form) {
            field {
                name = "Name"
                control {
                    input(name = "name", type = InputType.text) {
                        required = true
                        value = this@DiceFormWithInputs.name
                        if (failed) {
                            attributes["aria-invalid"] = "true"
                        }
                    }
                }
            }
            field {
                name = "Edges"
                help = "Each value must be on new line"
                control {
                    textArea {
                        name = "edges"
                        required = true
                        if (failed) {
                            attributes["aria-invalid"] = "true"
                        }
                        +edges.joinToString(separator = "\n")
                    }
                }
            }
            submit {
                value = "Save"
            }
        }
    }
}
