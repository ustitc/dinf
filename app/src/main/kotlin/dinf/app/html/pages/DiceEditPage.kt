package dinf.app.html.pages

import dinf.app.html.templates.DiceFormTemplate
import dinf.app.html.templates.Form
import dinf.app.html.templates.Layout
import dinf.app.html.templates.RollBlock
import dinf.app.routes.DiceResource
import dinf.app.services.DiceView
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input

class DiceEditPage(
    private val dice: DiceView,
    private val hasFailedForm: Boolean = false
) : Page {

    override fun Layout.apply() {
        val editURL = href(ResourcesFormat(), DiceResource.Edit(diceID = dice.id.print()))
        val deleteURL = href(ResourcesFormat(), DiceResource.Delete(diceID = dice.id.print()))

        content {
            h2 {
                +dice.name
            }

            insert(RollBlock(dice.edges.map { it.value })) {
                withResultOnTop = false
            }

            insert(DiceFormTemplate(Form(editURL))) {
                name = dice.name
                edges = dice.edges
                failed = hasFailedForm
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
