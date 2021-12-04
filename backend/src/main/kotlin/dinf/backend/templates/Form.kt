package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.input

class Form(private val url: String) : Template<FlowContent> {

    val field = PlaceholderList<FlowContent, FormField>()
    val submit = Placeholder<INPUT>()

    override fun FlowContent.apply() {
        form(action = url, method = FormMethod.post) {
            target = "_self"

            if (!field.isEmpty()) {
                each(field) {
                    insert(FormField()) {
                        insert(it)
                    }
                }
            }
            input(type = InputType.submit, classes = "button is-primary") {
                insert(submit)
            }
        }
    }
}
