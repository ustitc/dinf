package dinf.app.html.pages

import dinf.app.html.templates.Layout
import dinf.app.html.templates.RollBlock
import dinf.app.services.DiceView
import io.ktor.server.html.*
import kotlinx.html.h2

class DicePage(private val dice: DiceView) : Page {

    override fun Layout.apply() {
        content {
            h2 {
                +dice.name
            }

            insert(RollBlock(dice.edges.map { it.value })) {
                withResultOnTop = false
            }
        }
    }
}
