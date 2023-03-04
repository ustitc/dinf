package dinf.app.html.pages

import dev.ustits.hyperscript.hyperscript
import dinf.app.html.components.picoInlineButton
import dinf.app.html.templates.DiceFormTemplate
import dinf.app.html.templates.DiceCardTemplate
import dinf.app.html.templates.Form
import dinf.app.html.templates.Layout
import dinf.app.routes.DiceResource
import dinf.app.services.DiceView
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.input

class DiceEditPage(
    private val dice: DiceView,
    private val hasFailedForm: Boolean = false
) : Page {

    override fun Layout.apply() {
        val editURL = href(ResourcesFormat(), DiceResource.Edit(diceID = dice.id.print()))
        val deleteURL = href(ResourcesFormat(), DiceResource.Delete(diceID = dice.id.print()))

        content {
            insert(DiceCardTemplate(dice)) {
                name {
                    +dice.name
                }
            }

            picoInlineButton(classes = "secondary") {
                hyperscript = "on click toggle @hidden on #edit-form then toggle @hidden on me"
                +"Edit"
            }

            div {
                id = "edit-form"
                hidden = true

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
}
