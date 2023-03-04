package dinf.app.html.templates

import dinf.app.services.DiceView
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.h2

class DicePageTemplate(private val dice: DiceView) : Template<FlowContent> {

    val content = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 {
            +dice.name
        }
        insert(RollBlock(dice.edges.map { it.value })) {
            withResultOnTop = false
        }
        insert(content)
    }
}