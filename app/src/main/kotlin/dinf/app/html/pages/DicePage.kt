package dinf.app.html.pages

import dinf.app.html.templates.DicePageTemplate
import dinf.app.html.templates.Layout
import dinf.app.services.DiceView
import io.ktor.server.html.*

class DicePage(private val dice: DiceView) : Page {

    override fun Layout.apply() {
        content {
            insert(DicePageTemplate(dice)) {
            }
        }
    }
}
