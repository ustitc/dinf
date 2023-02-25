package dinf.app.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.app.html.EscapedString
import dinf.app.html.HTMLTextWithNewLines
import dinf.app.html.HtmlContent
import dinf.app.html.JSStringArray
import dinf.app.html.dataAttribute
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.P
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.style

class RollResult(
    private val _id: String,
    private val eventName: String,
    private val rollValues: List<String>
) : Template<FlowContent> {

    private val edgesAttr = "data-edges"

    override fun FlowContent.apply() {
        p("roll") {
            dataAttribute(edgesAttr, EdgesAttr(rollValues))
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