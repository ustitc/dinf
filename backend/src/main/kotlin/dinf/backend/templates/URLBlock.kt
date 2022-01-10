package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.p

class URLBlock(private val url: String) : Template<FlowContent> {

    var text: String = ""

    override fun FlowContent.apply() {
        p {
            +text
            a(href = url) { +url }
        }
    }
}
