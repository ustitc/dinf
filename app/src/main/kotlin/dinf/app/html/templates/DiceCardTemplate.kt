package dinf.app.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.app.html.EscapedString
import dinf.app.html.HTMLTextWithNewLines
import dinf.app.html.HtmlContent
import dinf.app.html.JSStringArray
import dinf.app.html.components.picoInlineButton
import dinf.app.html.dataAttribute
import dinf.app.services.DiceView
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.P
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.style

class DiceCardTemplate(dice: DiceView) : Template<FlowContent> {

    private val rollId = "result-${dice.id.print()}"
    private val rollValues = if (dice.edges.isEmpty()) {
        listOf("Dice is empty, add some values to start rolling!")
    } else {
        dice.edges.map { it.value }
    }
    private val edgesAttr = "data-edges"
    private val eventName = "roll"

    val name = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 {
            insert(name)
        }
        rollResult()
        rollButton()
    }

    private fun FlowContent.rollResult() {
        p("roll") {
            dataAttribute(edgesAttr, EdgesAttr(rollValues))
            withRenderedNewLineSymbols()
            hyperscript = """
                on $eventName get JSON.parse(@$edgesAttr)
                then put random it into me
                then repeat 2 times
                    toggle *opacity then settle
            """.trimIndent()

            this.id = rollId
        }
    }

    private fun FlowContent.rollButton() {
        p {
            picoInlineButton {
                hyperscript = "on click send $eventName to the #$rollId"
                +"Roll"
            }
        }
    }

    private fun P.withRenderedNewLineSymbols() {
        style = "white-space: pre-wrap;"
    }

    @JvmInline
    private value class EdgesAttr(private val values: List<String>) : HtmlContent {
        override fun print(): String {
            return JSStringArray(
                values.map {
                    HTMLTextWithNewLines(
                        EscapedString(it)
                    )
                }
            ).print()
        }
    }
}