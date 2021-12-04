package dinf.backend.templates

import dinf.domain.Dice
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.onClick

class HTMLDice(dice: Dice) : Template<FlowContent>, Dice by dice {

    override fun FlowContent.apply() {
        val rollValues = edges.stringList
            .joinToString(prefix = "[", postfix = "]") { "\"$it\"" }
        val diceTagID = "result-${id.nbString}"

        h1(classes = "title") { +name.nbString.toString() }
        h2(classes = "subtitle") { +"by <unknown>" }
        div(classes = "block") {
            button(classes = "button is-primary") {
                onClick = "roll($rollValues, \"$diceTagID\")"
                +"Roll"
            }
        }
        div(classes = "block") {
            this.id = diceTagID
        }
    }
}
