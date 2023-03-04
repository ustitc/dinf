package dinf.app.html.pages

import dinf.app.html.templates.DiceCardTemplate
import dinf.app.html.templates.LayoutTemplate
import dinf.app.services.DiceView
import io.ktor.server.html.*

class DicePage(private val dice: DiceView) : Page {

    override fun LayoutTemplate.apply() {
        content {
            insert(DiceCardTemplate(dice)) {
                name {
                    +dice.name
                }
            }
        }
    }
}
