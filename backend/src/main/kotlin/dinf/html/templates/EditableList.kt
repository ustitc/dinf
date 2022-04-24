package dinf.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.html.components.picoInlineButton
import dinf.html.text
import dinf.types.NBString
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.ol
import kotlinx.html.tabIndex
import kotlinx.html.textArea

class EditableList : Template<FlowContent> {

    var addButtonText: NBString = NBString("Add row")
    var items: List<String> = emptyList()
    var name = "row"

    override fun FlowContent.apply() {
        ol {
            items.forEach {
                li {
                    textArea {
                        name = this@EditableList.name
                        text(it)
                    }
                }
            }
            picoInlineButton(classes = "outline") {
                id = "add-row"
                tabIndex = "0"
                hyperscript = """
                            on click or keydown[key is 'Enter'] 
                            halt the event 
                            then make <li/> called li
                            then make <textarea/> called input
                            then set input.name to '${this@EditableList.name}'
                            then put input into li
                            then put li before target
                            then call input.focus()
                        """.trimIndent()
                text(addButtonText)
            }
        }
    }
}
