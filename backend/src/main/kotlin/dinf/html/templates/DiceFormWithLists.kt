package dinf.html.templates

import dinf.types.NBString
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.input

class DiceFormWithLists(private val form: Form) : DiceForm {

    override var name: String = ""
    override var edges: List<String> = emptyList()
    override var failed: Boolean = false

    override fun FlowContent.apply() {
        insert(form) {
            field {
                name = "Dice Name"
                control {
                    input(name = "name", type = InputType.text) {
                        required = true
                        autoFocus = true
                        value = this@DiceFormWithLists.name
                        if (failed) {
                            attributes["aria-invalid"] = "true"
                        }
                    }
                }
            }
            field {
                name = "Edges"
                control {
                    insert(EditableList()) {
                        name = "edges"
                        addButtonText = NBString("Add edge")
                        items = edges
                    }
                }
            }
            submit {
                value = "Create Dice"
            }
        }
    }
}