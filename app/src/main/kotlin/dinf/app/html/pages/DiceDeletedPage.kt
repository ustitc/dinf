package dinf.app.html.pages

import dinf.app.html.templates.Layout
import kotlinx.html.p

class DiceDeletedPage : Page {

    override fun Layout.apply() {
        content {
            p { +"Dice deleted" }
        }
    }
}
