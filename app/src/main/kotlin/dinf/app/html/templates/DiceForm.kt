package dinf.app.html.templates

import dinf.domain.Edge
import dinf.types.NBString
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.input

class DiceForm(private val form: Form) : Template<FlowContent> {

    var name: String = ""
    var edges: List<Edge> = emptyList()
    var failed: Boolean = false

    val submit = Placeholder<INPUT>()

    override fun FlowContent.apply() {
        insert(form) {
            field {
                name = "Dice Name"
                control {
                    input(name = "name", type = InputType.text) {
                        required = true
                        autoFocus = true
                        value = this@DiceForm.name
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
                        items = edges.map { EditableList.Item(it.id.print(), it.value) }
                    }
                }
            }
            submit {
                insert(this@DiceForm.submit)
            }
        }
    }
}
