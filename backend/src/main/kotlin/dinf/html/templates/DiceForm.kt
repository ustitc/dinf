package dinf.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent

interface DiceForm : Template<FlowContent> {

    var name: String
    var edges: List<String>
    var failed: Boolean

}
