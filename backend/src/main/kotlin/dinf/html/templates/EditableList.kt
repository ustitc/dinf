package dinf.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.html.text
import dinf.types.NBString
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.li
import kotlinx.html.ul
import kotlinx.html.a
import kotlinx.html.contentEditable
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.role

class EditableList : Template<FlowContent> {

    var welcomeText: NBString = NBString("Tap on me")
    var addButtonText: NBString = NBString("Add row")

    override fun FlowContent.apply() {
        ul {
            hyperscript = """
                    on keydown[key is 'Enter'] halt the event
                        then make <li/>
                        then set it.contentEditable to 'true'
                        then put it after target
                        then call it.focus()
                    on keydown[key is 'Backspace']
                        if no target.textContent
                            if me.children.length > 2
                                get previous <li/> from target
                                if no it
                                    get next <li/> from target
                                end
                            call it.focus()
                            then remove target
                            end
                        end
                    on keydown[key is 'ArrowUp']
                        get previous <li/> from target
                        if no it == false
                            call it.focus()
                        end
                    on keydown[key is 'ArrowDown']
                        get next <li/> from target
                        if no it == false
                            call it.focus()
                        end
                """.trimIndent()
            li {
                hyperscript = """
                            on click 1
                                set my.textContent to ''
                                then toggle @hidden on #add-row
                            """.trimIndent()
                contentEditable = true
                text(welcomeText)
            }
            a(classes = "secondary outline") {
                id = "add-row"
                hyperscript = """
                            on click make <li/>
                                then set it.contentEditable to 'true'
                                then put it before target
                                then call it.focus()
                        """.trimIndent()
                role = "button"
                hidden = true
                text(addButtonText)
            }
        }
    }
}
