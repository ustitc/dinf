package dinf.html.templates

import dinf.domain.Dice
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.role
import kotlin.random.Random

class RollButton(val dice: Dice) : Template<FlowContent> {

    var resultTagID = "result-${Random.nextInt()}"

    override fun FlowContent.apply() {
        val rollValues = dice.edges.stringList
            .joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

        p {
            a {
                role = "button"
                onClick = "roll($rollValues, \"$resultTagID\")"
                +"Roll"
            }
        }
        p {
            this.id = resultTagID
        }
    }
}
