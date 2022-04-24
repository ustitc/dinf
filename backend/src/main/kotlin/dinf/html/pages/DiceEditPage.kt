package dinf.html.pages

import dev.ustits.hyperscript.hyperscript
import dinf.domain.Dice
import dinf.html.components.picoInlineButton
import dinf.html.components.picoModal
import dinf.html.templates.DiceForm
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import io.ktor.html.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.footer
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.hGroup
import kotlinx.html.input
import kotlinx.html.p

class DiceEditPage(
    private val form: DiceForm,
    private val dice: Dice,
    private val editURL: String,
    private val deleteURL: String
) : Page {

    var dialogOpen: Boolean = true

    override fun Layout.apply() {
        content {
            hGroup {
                h2 { +dice.name.print() }
                h3 {
                    text("Save this link to edit your dice later: ")
                    a(href = editURL) { +editURL }
                }
            }

            picoModal(dialogOpen) {
                h3 { +"Beware!" }
                p {
                    +"Save this link to edit your dice later: "
                    a(href = editURL) { +editURL }
                }
                footer {
                    picoInlineButton("contrast") {
                        hyperscript = """
                                    on click or keydown[key is 'Enter'] 
                                    halt the event 
                                    then toggle @open on <dialog/>  
                                """.trimIndent()
                        +"Ok"
                    }
                }
            }

            insert(RollBlock(dice)) {
            }

            insert(form) {
                name = dice.name.print()
                edges = dice.edges.toStringList()
            }

            form(action = deleteURL, method = FormMethod.post) {
                input(type = InputType.submit, classes = "delete") {
                    value = "Delete"
                }
            }
        }
    }
}
