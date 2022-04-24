package dinf.html.pages

import dinf.html.templates.Layout
import kotlinx.html.p

class DiceDeletedPage : Page {

    override fun Layout.apply() {
        content {
            p { +"Dice deleted" }
        }
    }
}
