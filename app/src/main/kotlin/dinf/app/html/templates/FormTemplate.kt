package dinf.app.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.INPUT
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.input

class FormTemplate(private val url: String) : Template<FlowContent> {

    val field = PlaceholderList<FlowContent, FormFieldTemplate>()
    val submit = Placeholder<INPUT>()

    override fun FlowContent.apply() {
        form(action = url, method = FormMethod.post) {
            target = "_self"

            if (!field.isEmpty()) {
                each(field) {
                    insert(FormFieldTemplate()) {
                        insert(it)
                    }
                }
            }
            input(type = InputType.submit) {
                insert(submit)
            }
        }
    }
}
