package dinf.html.pages

import dinf.html.templates.DiceFormWithLists
import dinf.html.templates.Form
import dinf.html.templates.Layout
import dinf.routes.DiceResource
import io.ktor.server.html.*

class DiceCreatePage(
    private val url: String,
    private val resource: DiceResource.New
) : Page {

    override fun Layout.apply() {
        content {
            insert(DiceFormWithLists(Form(url))) {
                failed = resource.isFailed ?: false
            }
        }
    }
}
