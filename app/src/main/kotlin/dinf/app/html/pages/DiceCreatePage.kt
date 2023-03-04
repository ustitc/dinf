package dinf.app.html.pages

import dinf.app.html.templates.DiceFormTemplate
import dinf.app.html.templates.FormTemplate
import dinf.app.html.templates.LayoutTemplate
import dinf.app.routes.DiceResource
import io.ktor.server.html.*

class DiceCreatePage(
    private val url: String,
    private val resource: DiceResource.New
) : Page {

    override fun LayoutTemplate.apply() {
        content {
            insert(DiceFormTemplate(FormTemplate(url))) {
                failed = resource.isFailed ?: false
            }
        }
    }
}
