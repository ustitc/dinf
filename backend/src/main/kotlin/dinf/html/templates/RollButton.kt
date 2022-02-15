package dinf.html.templates

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.html.EscapedString
import dinf.html.JSStringArray
import dinf.html.dataAttribute
import dev.ustits.hyperscript.hyperscript
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.role
import kotlin.random.Random

class RollButton(val dice: Dice) : Template<FlowContent> {

    private val edgesAttr = "data-edges"

    var resultTagID = "result-${Random.nextInt()}"

    override fun FlowContent.apply() {
        p {
            a {
                role = "button"
                hyperscript = "on click send roll to the #$resultTagID"
                +"Roll"
            }
        }
        p("roll") {
            dataAttribute(edgesAttr, EdgesAttr(dice.edges))
            hyperscript = """
                on roll get JSON.parse(@$edgesAttr)
                then put random it into me
                then repeat 2 times
                    toggle *opacity then settle
            """.trimIndent()

            this.id = resultTagID
        }
    }

    @JvmInline
    private value class EdgesAttr(private val edges: Edges): dinf.html.HtmlContent {
        override fun print(): String {
            return JSStringArray(
                edges.stringList.map { EscapedString(it) }
            ).print()
        }
    }
}
