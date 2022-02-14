package dinf.html.templates

import dinf.domain.Dice
import dinf.hyperscript.hyperscript
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id
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
                hyperscript = "on click send roll to the #$resultTagID"
                +"Roll"
            }
        }
        p("roll") {
            hyperscript = """
                on roll get roll($rollValues) then put it into me
                then repeat 2 times
                    toggle *opacity then settle
            """.trimIndent()

            this.id = resultTagID
        }
    }
}
