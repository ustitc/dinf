package dinf.app.html.templates

import dinf.app.services.EdgeView
import dinf.types.NBString
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.input

class DiceFormTemplate(private val form: Form) : Template<FlowContent> {

    var name: String = ""
    var edges: List<EdgeView> = emptyList()
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
                        value = this@DiceFormTemplate.name
                        if (failed) {
                            attributes["aria-invalid"] = "true"
                        }
                    }
                }
            }
            field {
                name = "Edges"
                control {
                    insert(EditableListTemplate()) {
                        name = "edges"
                        addButtonText = NBString("Add edge")
                        items = edges.map { EditableListTemplate.Item(it.id.print(), it.value) }
                    }
                }
            }
            submit {
                insert(this@DiceFormTemplate.submit)
            }
        }
    }
}
