package dinf.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.domain.Dice
import dinf.domain.Edges
import dinf.html.EscapedString
import dinf.html.HTMLTextWithNewLines
import dinf.html.HtmlContent
import dinf.html.JSStringArray
import dinf.html.dataAttribute
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.P
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.style

class RollResult(
    private val _id: String,
    private val eventName: String,
    private val dice: Dice
) : Template<FlowContent> {

    private val edgesAttr = "data-edges"

    override fun FlowContent.apply() {
        p("roll") {
            dataAttribute(edgesAttr, EdgesAttr(dice.edges))
            withRenderedNewLineSymbols()
            hyperscript = """
                on $eventName get JSON.parse(@$edgesAttr)
                then put random it into me
                then repeat 2 times
                    toggle *opacity then settle
            """.trimIndent()

            this.id = _id
        }
    }

    private fun P.withRenderedNewLineSymbols() {
        style = "white-space: pre-wrap;"
    }

    @JvmInline
    private value class EdgesAttr(private val edges: Edges) : HtmlContent {
        override fun print(): String {
            return JSStringArray(
                edges.toStringList().map {
                    HTMLTextWithNewLines(
                        EscapedString(it)
                    )
                }
            ).print()
        }
    }
}