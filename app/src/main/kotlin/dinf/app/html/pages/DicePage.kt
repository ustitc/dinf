package dinf.app.html.pages

import dinf.domain.Dice
import dinf.app.html.templates.Layout
import dinf.app.html.templates.RollBlock
import io.ktor.server.html.*
import kotlinx.html.h2

class DicePage(private val dice: Dice) : Page {

    override fun Layout.apply() {
        content {
            h2 {
                +dice.name.print()
            }

            insert(RollBlock(dice.edges.map { it.value })) {
                withResultOnTop = false
            }
        }
    }
}
