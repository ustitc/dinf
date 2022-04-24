package dinf.html.pages

import dev.ustits.hyperscript.hyperscript
import dinf.domain.Dice
import dinf.html.components.picoInlineButton
import dinf.html.components.picoModal
import dinf.html.templates.DiceFormWithLists
import dinf.html.templates.Form
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import dinf.routes.DiceResource
import io.ktor.server.html.*
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
    private val dice: Dice,
    private val editURL: String,
    private val deleteURL: String,
    private val resource: DiceResource.Edit
) : Page {

    override fun Layout.apply() {
        val isOpenDialog = resource.isFirstTime ?: false

        content {
            hGroup {
                h2 { +dice.name.print() }
                h3 {
                    text("Save this link to edit your dice later: ")
                    a(href = editURL) { +editURL }
                }
            }

            picoModal(isOpenDialog) {
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

            insert(DiceFormWithLists(Form(editURL))) {
                name = dice.name.print()
                edges = dice.edges.toStringList()
                form {
                    failed = resource.isFailed ?: false
                    submit {
                        value = "Save changes"
                    }
                }
            }

            form(action = deleteURL, method = FormMethod.post) {
                input(type = InputType.submit, classes = "delete") {
                    value = "Delete"
                }
            }
        }
    }
}
