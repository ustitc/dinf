package dinf.app.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.app.html.components.picoInlineButton
import dinf.app.html.text
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
    var items: List<Item> = emptyList()
    var name = "row"

    data class Item(val id: String, val value: String)

    override fun FlowContent.apply() {
        ol {
            items.forEach {
                li {
                    textArea {
                        id = it.id
                        name = this@EditableList.name
                        text(it.value)
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
