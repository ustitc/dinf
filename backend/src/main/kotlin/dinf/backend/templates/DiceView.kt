package dinf.backend.templates

import dinf.domain.Dice
import dinf.domain.ID
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.onClick

class DiceView(private val dice: Dice, val id: ID) : Template<FlowContent> {

    override fun FlowContent.apply() {
        val rollValues = dice.edges.stringList
            .joinToString(prefix = "[", postfix = "]") { "\"$it\"" }
        val resultTagID = "result-${id.print()}"

        h1 { +dice.name.nbString.toString() }
        button {
            onClick = "roll($rollValues, \"$resultTagID\")"
            +"Roll"
        }
        div {
            this.id = resultTagID
        }
    }
}
