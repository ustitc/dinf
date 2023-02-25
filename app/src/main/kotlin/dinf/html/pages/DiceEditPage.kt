package dinf.html.pages

import dinf.domain.Dice
import dinf.html.templates.DiceForm
import dinf.html.templates.Form
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import dinf.routes.DiceResource
import io.ktor.server.html.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input

class DiceEditPage(
    private val dice: Dice,
    private val editURL: String,
    private val deleteURL: String,
    private val resource: DiceResource.Edit
) : Page {

    override fun Layout.apply() {
        content {
            h2 {
                +dice.name.print()
            }

            insert(RollBlock(dice.edges.toStringList())) {
                withResultOnTop = false
            }

            insert(DiceForm(Form(editURL))) {
                name = dice.name.print()
                edges = dice.edges.toStringList()
                failed = resource.isFailed ?: false
                submit {
                    value = "Save changes"
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
