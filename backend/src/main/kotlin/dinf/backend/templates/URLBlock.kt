package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div

class URLBlock(private val url: String) : Template<FlowContent> {

    var text: String = ""

    override fun FlowContent.apply() {
        div("block") {
            +text
            a(href = url) { +url }
        }
    }
}
