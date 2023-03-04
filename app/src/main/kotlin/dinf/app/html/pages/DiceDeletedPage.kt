package dinf.app.html.pages

import dinf.app.html.templates.LayoutTemplate
import kotlinx.html.p

class DiceDeletedPage : Page {

    override fun LayoutTemplate.apply() {
        content {
            p { +"Dice deleted" }
        }
    }
}
