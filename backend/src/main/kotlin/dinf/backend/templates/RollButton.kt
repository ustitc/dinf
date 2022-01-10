package dinf.backend.templates

import dinf.domain.Dice
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.onClick
import kotlin.random.Random

class RollButton(val dice: Dice) : Template<FlowContent> {

    var resultTagID = "result-${Random.nextInt()}"

    override fun FlowContent.apply() {
        val rollValues = dice.edges.stringList
            .joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

        button {
            onClick = "roll($rollValues, \"$resultTagID\")"
            +"Roll"
        }
        div {
            this.id = resultTagID
        }
    }
}
